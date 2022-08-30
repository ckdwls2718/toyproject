package spring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestEpilogueDto {

    @NotBlank
    private Long boardId;

    @NotBlank
    private String content;
}
