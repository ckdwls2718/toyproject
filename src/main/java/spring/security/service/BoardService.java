package spring.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.RequestBoardDto;
import spring.security.entity.Board;
import spring.security.entity.BoardImage;
import spring.security.entity.Category;
import spring.security.repository.BoardImageRepository;
import spring.security.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;


    //이 부분 나중에 다른 로직으로 바꿔보기
    @Transactional
    public Long save(RequestBoardDto boardDto, List<String> urlList) {

        Board savedBoard = boardRepository.save(dtoToEntity(boardDto));

        for (String url : urlList) {
            BoardImage boardImage = BoardImage.builder()
                    .url(url)
                    .board(savedBoard)
                    .build();
            boardImageRepository.save(boardImage);
        }

        return savedBoard.getId();
    }

    //S3에서도 파일을 삭제하는 로직 추가해보기기
    @Transactional
    public List<String> delete(Long boardId) {
       List<String> boardImageList = boardImageRepository.findByBoardId(boardId)
               .orElseThrow(() -> new RuntimeException("해당 게시물의 이미지를 찾을 수 없습니다"));

       boardRepository.deleteById(boardId);

       return boardImageList;
    }

    private Board dtoToEntity(RequestBoardDto boardDto) {

        Category category = Category.builder().id(boardDto.getCategoryId()).build();

        return Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .cheerCount(0L)
                .nowDonation(0L)
                .targetDonation(boardDto.getTargetDonation())
                .startDate(boardDto.getStartDate())
                .finishDate(boardDto.getFinishDate())
                .category(category)
                .build();
    }
}
