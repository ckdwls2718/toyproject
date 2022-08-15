package spring.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.MemberRequestDto;
import spring.security.dto.MemberResponseDto;
import spring.security.entity.Member;
import spring.security.repository.MemberRepository;
import spring.security.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save(MemberRequestDto dto) {
        Member saveMember = Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
        memberRepository.save(saveMember);
    }

    public MemberResponseDto getMemberInfo(String email) {
        return memberRepository.findById(email)
                .map(entity -> new MemberResponseDto(entity.getEmail()))
                .orElseThrow(() -> new RuntimeException(("유저정보가 없습니다")));
    }

    public MemberResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberEmail())
                .map(entity -> new MemberResponseDto(entity.getEmail()))
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
}
