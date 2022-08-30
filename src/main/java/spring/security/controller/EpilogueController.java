package spring.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.security.dto.RequestEpilogueDto;
import spring.security.dto.ResponseEpilogueDto;
import spring.security.service.EpilogueService;
import spring.security.service.S3Uploader;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/epilogue")
public class EpilogueController {

    private final EpilogueService epilogueService;
    private final S3Uploader s3Uploader;
    private final String dirname = "test";

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> save(@RequestPart("images") List<MultipartFile> multipartFileList,
                                       @RequestPart("requestDto") @Valid RequestEpilogueDto epilogueDto) {

        List<String> failUrlList = null;

        try {
            List<String> imageUrlList = s3Uploader.uploadFiles(multipartFileList, dirname);
            failUrlList = imageUrlList.stream().collect(Collectors.toList());

            epilogueService.save(epilogueDto, imageUrlList);

            return ResponseEntity.ok().body("success");

        } catch (Exception e) {

            s3Uploader.deleteFiles(failUrlList, dirname);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseEpilogueDto> findOne(@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok().body(epilogueService.findOne(boardId));
    }

}
