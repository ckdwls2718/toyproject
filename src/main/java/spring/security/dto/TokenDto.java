package spring.security.dto;

import lombok.*;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private long accessTokenExpiresIn;
    private String refreshToken;
}
