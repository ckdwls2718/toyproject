package spring.security.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CommentDto {

    private Long id;

    @NotBlank
    private String content;

    @NotBlank
    private String nickname;

    @NotBlank
    private Long donation;
}
