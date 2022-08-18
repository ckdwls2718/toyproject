package spring.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.MemberRequestDto;
import spring.security.dto.MemberResponseDto;
import spring.security.dto.PWDto;
import spring.security.entity.Member;
import spring.security.repository.MemberRepository;
import spring.security.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(MemberRequestDto dto) {
        Member saveMember = Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
        memberRepository.save(saveMember);
    }

    @Transactional
    public MemberResponseDto changeNickname(MemberRequestDto requestDto) {

        if (duplicateCheckByNickname(requestDto.getNickname())){
            throw new RuntimeException("이미 사용중인 닉네임입니다");
        }

        Member findMember = memberRepository.findById(getMyInfo().getEmail())
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        findMember.changeNickname(requestDto.getNickname());

        return new MemberResponseDto(findMember.getEmail(), findMember.getNickname());

    }

    @Transactional
    public MemberResponseDto changePassword(PWDto dto) {

        Member findMember = memberRepository.findById(getMyInfo().getEmail())
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        if (!passwordEncoder.matches(dto.getBeforePW(), findMember.getPassword())) {
            throw new RuntimeException("기존 비밀번호가 일치하지 않습니다");
        }

        findMember.changePassword(passwordEncoder.encode(dto.getNewPW()));

        return new MemberResponseDto(findMember.getEmail(), findMember.getNickname());
    }

    public MemberResponseDto getMemberInfo(String email) {
        return memberRepository.findById(email)
                .map(entity -> new MemberResponseDto(entity.getEmail(), entity.getNickname()))
                .orElseThrow(() -> new RuntimeException(("유저정보가 없습니다")));
    }

    public MemberResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberEmail())
                .map(entity -> new MemberResponseDto(entity.getEmail(), entity.getNickname()))
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    private boolean duplicateCheckByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
