package spring.security.service;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.CommentDto;
import spring.security.dto.RequestBoardDto;
import spring.security.dto.ResponseBoardDto;
import spring.security.entity.Board;
import spring.security.entity.BoardImage;
import spring.security.entity.Category;
import spring.security.entity.Comment;
import spring.security.repository.BoardImageRepository;
import spring.security.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    // 자기게시물만 삭제할 수 있는 권한 추가하기
    @Transactional
    public List<String> delete(Long boardId) {
       List<String> boardImageList = boardImageRepository.findByBoardId(boardId)
               .orElseThrow(() -> new RuntimeException("해당 게시물의 이미지를 찾을 수 없습니다"));

       boardRepository.deleteById(boardId);

       return boardImageList;
    }

    public List<ResponseBoardDto> findAll() {

        List<ResponseBoardDto> boardDtoList = new ArrayList<>();

        List<Board> boardList = boardRepository.findAll();

        for (Board board : boardList) {
            boardDtoList.add(entityToDto(board));
        }

        return boardDtoList;
    }

    public ResponseBoardDto findOne(Long boardId) {

        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("해당 게시물을 찾을 수 없습니다"));

        return entityToDto(findBoard);
    }

    @Transactional
    public void plusDonation(Long boardId, Long donation) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다"));

        findBoard.plusDonation(donation);
    }

    @Transactional
    public void minusDonation(Long boardId, Long donation) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다"));

        findBoard.minusDonation(donation);
    }

    @Transactional
    public List<String> modify(RequestBoardDto boardDto, List<String> urlList) {

        List<String> imageUrlList = new ArrayList<>();

        Board findBoard = boardRepository.findById(boardDto.getId())
                .orElseThrow(() -> new RuntimeException("해당 게시물을 찾을 수 없습니다"));

        for (BoardImage boardImage : findBoard.getBoardImageList()) {
            imageUrlList.add(boardImage.getUrl());
        }
        boardImageRepository.deleteByBoardId(boardDto.getId());

        findBoard.changeBoard(boardDto);

        for (String url : urlList) {
            BoardImage boardImage = BoardImage.builder()
                    .url(url)
                    .board(findBoard)
                    .build();
            boardImageRepository.save(boardImage);
        }

        return imageUrlList;
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

    private ResponseBoardDto entityToDto(Board board) {

        List<String> imageUrlList = new ArrayList<>();

        Set<BoardImage> boardImageList = board.getBoardImageList();
        for (BoardImage boardImage : boardImageList) {
            imageUrlList.add(boardImage.getUrl());
        }

        List<CommentDto> commentDtoList = new ArrayList<>();

        Set<Comment> commentList = board.getCommentList();
        for (Comment comment : commentList) {
            commentDtoList.add(CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .nickname(comment.getMember().getNickname())
                    .donation(comment.getDonation())
                    .build());
        }

        return ResponseBoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .imageUrlList(imageUrlList)
                .comment(commentDtoList)
                .content(board.getContent())
                .nowDonation(board.getNowDonation())
                .targetDonation(board.getTargetDonation())
                .startDate(board.getStartDate().toString())
                .finishDate(board.getFinishDate().toString())
                .cheerCount(board.getCheerCount())
                .category(board.getCategory().getName())
                .build();
    }
}
