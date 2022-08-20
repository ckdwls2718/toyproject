package spring.security.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.dto.MemberRequestDto;
import spring.security.dto.MemberResponseDto;
import spring.security.dto.TokenDto;
import spring.security.dto.TokenRequestDto;
import spring.security.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto dto) {
        return ResponseEntity.ok(authService.singUp(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto dto) {
        return ResponseEntity.ok(authService.reissue(dto));
    }

    @GetMapping("/hi")
    public ResponseEntity<String> hi(){
        return ResponseEntity.ok("hi");
    }

}
