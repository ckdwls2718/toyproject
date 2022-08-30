package spring.security.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseEpilogueDto {

    private Long id;

    private String content;
}
