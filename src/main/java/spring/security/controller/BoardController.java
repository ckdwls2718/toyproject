package spring.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.security.dto.CommentDto;
import spring.security.dto.RequestBoardDto;
import spring.security.dto.ResponseBoardDto;
import spring.security.service.BoardService;
import spring.security.service.CommentService;
import spring.security.service.S3Uploader;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final S3Uploader s3Uploader;
    private final String dirname = "test";


    @GetMapping("")

    public ResponseEntity<List<ResponseBoardDto>> findAll() {
        return ResponseEntity.ok().body(boardService.findAll());
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseBoardDto> findOne(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok().body(boardService.findOne(boardId));
    }

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> save(@RequestPart("images") List<MultipartFile> multipartFileList,
                                       @RequestPart("requestDto") @Valid RequestBoardDto boardDto) {

        List<String> failUrlList = null;

        try {
            List<String> urlList = s3Uploader.uploadFiles(multipartFileList, dirname);
            failUrlList = urlList.stream().collect(Collectors.toList());

            Long saveBoardId = boardService.save(boardDto, urlList);

            return ResponseEntity.ok().body("save boardId : " + saveBoardId);

        } catch (Exception e) {
            s3Uploader.deleteFiles(failUrlList, dirname);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{boardId}/modify")
    public ResponseEntity<ResponseBoardDto> findOneForModify(@PathVariable("boardId") Long boardId) {
        ResponseBoardDto findBoard = boardService.findOne(boardId);
        LocalDate startDate = LocalDate.parse(findBoard.getStartDate());

        if (startDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("already started");
        }

        return ResponseEntity.ok().body(findBoard);
    }

    // 수정은 기간 시작전에만 가능하다.
    @PutMapping(value = "/modify", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> modify(@RequestPart("images") List<MultipartFile> multipartFileList,
                                         @RequestPart("requestDto") @Valid RequestBoardDto boardDto) {

        List<String> failUrlList = null;

        try {
            List<String> urlList = s3Uploader.uploadFiles(multipartFileList, dirname);
            failUrlList = urlList.stream().collect(Collectors.toList());

            List<String> deleteUrlList = boardService.modify(boardDto, urlList);

            s3Uploader.deleteFiles(deleteUrlList, dirname);

            return ResponseEntity.ok().body("modified boardId :" + boardDto.getId());

        } catch (Exception e) {
            s3Uploader.deleteFiles(failUrlList, dirname);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> delete(@PathVariable Long boardId) {
        try {
            List<String> urlList = boardService.delete(boardId);

            s3Uploader.deleteFiles(urlList, dirname);

            return ResponseEntity.ok().body("Success Delete");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/{boardId}/comment")
    public ResponseEntity<String> saveComment(@PathVariable("boardId") Long boardId,
                                              @RequestBody @Valid CommentDto commentDto) {

        Long donation = commentService.save(boardId, commentDto);

        boardService.plusDonation(boardId, donation);

        return ResponseEntity.ok().body("id:" + boardId + " board comment save");

    }

    @DeleteMapping("/{boardId}/comment")
    public ResponseEntity<String> deleteComment(@PathVariable("boardId") Long boardId,
                                                @RequestBody @Valid CommentDto commentDto) {

        commentService.delete(commentDto.getId());

        boardService.minusDonation(boardId, commentDto.getDonation());

        return ResponseEntity.ok().body("id:" + boardId + " board comment delete");
    }
}
