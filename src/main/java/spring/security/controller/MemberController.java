package spring.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.dto.MemberRequestDto;
import spring.security.dto.MemberResponseDto;
import spring.security.dto.PWDto;
import spring.security.service.MemberService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @GetMapping("/{email}")
    public ResponseEntity<MemberResponseDto> getMemberInfo(@PathVariable("email") String email) {
        return ResponseEntity.ok(memberService.getMemberInfo(email));
    }

    @PutMapping("/changeNick")
    public ResponseEntity<MemberResponseDto> changeNickname(@RequestBody @Valid MemberRequestDto requestDto) {
        return ResponseEntity.ok(memberService.changeNickname(requestDto));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<MemberResponseDto> changePassword(@RequestBody @Valid PWDto dto) {
        return ResponseEntity.ok(memberService.changePassword(dto));
    }

}
