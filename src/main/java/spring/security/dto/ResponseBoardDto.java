package spring.security.dto;

import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResponseBoardDto {

    private Long id;
    private String title;
    private String content;
    private List<String> imageUrlList;
    private Long nowDonation;
    private Long targetDonation;
    private String startDate;
    private String finishDate;
    private String category;
    private List<CommentDto> comment;
    private Long cheerCount;
}
