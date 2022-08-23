package spring.security.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long donation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

}
