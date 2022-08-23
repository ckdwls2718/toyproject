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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final S3Uploader s3Uploader;

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
                                       @RequestPart("requestDto") RequestBoardDto boardDto) {
        try {
            List<String> urlList = s3Uploader.uploadFiles(multipartFileList, "test");

            Long saveBoardId = boardService.save(boardDto, urlList);

            return ResponseEntity.ok().body("save boardId : " + saveBoardId);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> delete(@PathVariable Long boardId) {
        try {
            List<String> urlList = boardService.delete(boardId);

            for (String url : urlList) {
                String[] split = url.split("/");
                String filename = "test/" + split[split.length - 1];
                s3Uploader.deleteS3(filename);
            }

            return ResponseEntity.ok().body("Success Delete");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/{boardId}/comment")
    public ResponseEntity<String> saveComment(@PathVariable("boardId") Long boardId, CommentDto commentDto) {

        Long donation = commentService.save(boardId, commentDto);

        boardService.plusDonation(boardId, donation);

        return ResponseEntity.ok().body(boardId + "번 게시물에 댓글이 등록되었습니다");

    }

    @DeleteMapping("/{boardId}/comment")
    public ResponseEntity<String> deleteComment(@PathVariable("boardId") Long boardId,
                                                @RequestBody CommentDto commentDto) {

        commentService.delete(commentDto.getId());

        boardService.minusDonation(boardId, commentDto.getDonation());

        return ResponseEntity.ok().body(boardId + "번 게시물의 댓글이 삭제되었습니다");
    }
}
