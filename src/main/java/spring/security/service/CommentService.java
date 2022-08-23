package spring.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.CommentDto;
import spring.security.entity.Board;
import spring.security.entity.Comment;
import spring.security.entity.Member;
import spring.security.repository.CommentRepository;
import spring.security.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Long save(Long boardId, CommentDto commentDto) {

        Board board = Board.builder()
                .id(boardId)
                .build();

        Member member = Member.builder()
                .email(SecurityUtil.getCurrentMemberEmail())
                .build();

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .board(board)
                .donation(commentDto.getDonation())
                .member(member)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return savedComment.getDonation();
    }

    // 작성자만 삭제할 수 있도록 추가
    @Transactional
    public void delete(Long commentId) {

        commentRepository.deleteById(commentId);
    }
}
