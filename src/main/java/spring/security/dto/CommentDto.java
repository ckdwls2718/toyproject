package spring.security.dto;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CommentDto {

    private Long id;
    private String content;
    private String nickname;
    private Long donation;
}
