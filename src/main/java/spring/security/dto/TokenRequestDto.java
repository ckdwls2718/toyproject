package spring.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class TokenRequestDto {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
