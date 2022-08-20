package spring.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.security.dto.RequestBoardDto;
import spring.security.service.BoardService;
import spring.security.service.S3Uploader;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final S3Uploader s3Uploader;

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> save(@RequestPart("images") List<MultipartFile> multipartFileList,
                                       @RequestPart("requestDto") RequestBoardDto boardDto) {
        try {
            List<String> urlList = s3Uploader.uploadFiles(multipartFileList, "test");

            Long saveBoardId = boardService.save(boardDto, urlList);

            return ResponseEntity.ok().body("저장된 boardId : " + saveBoardId);

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

            return ResponseEntity.ok().body("삭제되었습니다");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
