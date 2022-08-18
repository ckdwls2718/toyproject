package spring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import spring.security.entity.Member;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    private String email;
    private String nickname;

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getEmail(), member.getNickname());
    }
}
