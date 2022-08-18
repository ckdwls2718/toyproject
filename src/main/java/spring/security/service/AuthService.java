package spring.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.MemberRequestDto;
import spring.security.dto.MemberResponseDto;
import spring.security.dto.TokenDto;
import spring.security.dto.TokenRequestDto;
import spring.security.entity.Member;
import spring.security.entity.RefreshToken;
import spring.security.jwt.TokenProvider;
import spring.security.repository.MemberRepository;
import spring.security.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberResponseDto singUp(MemberRequestDto dto) {
        if (memberRepository.existsById(dto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        } else if (memberRepository.existsByNickname(dto.getNickname())){
            throw new RuntimeException("이미 사용중인 닉네임입니다.");
        }

        Member member = dto.toMember(passwordEncoder);

        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public TokenDto login(MemberRequestDto dto) {
        // login ID, PW 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();

        // 실제로 검증
        // authentication 메서드가 실행될 때 CustomUserDetailsService에서 만든 loadByUsername 메서드가 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authenticate);

        // RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authenticate.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        
        //토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto dto) {
        // RefreshToken 검증
        if (!tokenProvider.validateToken(dto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다");
        }
        
        // access token에서 Member Id  가져오기
        Authentication authentication = tokenProvider.getAuthentication(dto.getAccessToken());
        
        // 저장소에서 Member Id를 기반으로 refresh Token을 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자 입니다."));

        // refresh token이 일치하는지 검사
        if (!refreshToken.getValue().equals(dto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다");
        }
        
        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        
        // 저장소 정보 업데이트
        // 더티 체킹
        refreshToken.updateValue(tokenDto.getRefreshToken());

        return tokenDto;
    }
}
