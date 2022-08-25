package spring.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.entity.Board;
import spring.security.entity.Cheer;
import spring.security.entity.Member;
import spring.security.repository.BoardRepository;
import spring.security.repository.CheerRepository;
import spring.security.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheerService {

    private final CheerRepository cheerRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void save(Long boardId) {

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();

        if (duplicate(loginMemberEmail, boardId)){
            throw new RuntimeException("이미 응원하기를 누르셨습니다");
        }

        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다"));

        Member loginMember = Member.builder()
                .email(loginMemberEmail)
                .build();

        cheerRepository.save(
                Cheer.builder()
                .board(findBoard)
                .member(loginMember)
                .build());

        findBoard.plusCheer();
    }

    @Transactional
    public void delete(Long boardId) {

        String loginMemberEmail = SecurityUtil.getCurrentMemberEmail();

        cheerRepository.deleteByMemberEmailAndBoardId(loginMemberEmail, boardId);

        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시물을 불러올 수 없습니다"));

        findBoard.minusCheer();

    }

    private boolean duplicate(String email, Long boardId) {
        return cheerRepository.existsByMemberEmailAndBoardId(email, boardId);
    }
}
