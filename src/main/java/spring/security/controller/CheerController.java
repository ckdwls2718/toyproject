package spring.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.dto.RequestBoardDto;
import spring.security.service.CheerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cheer")
public class CheerController {

    private final CheerService cheerService;

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody RequestBoardDto boardDto) {
        cheerService.save(boardDto.getId());

        return ResponseEntity.ok("save cheer");
    }

    @DeleteMapping("")
    public ResponseEntity<String> delete(@RequestBody RequestBoardDto boardDto) {
        cheerService.delete(boardDto.getId());

        return ResponseEntity.ok("delete cheer");
    }
}
