package spring.security.entity;

import lombok.*;
import spring.security.dto.RequestBoardDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long cheerCount;

    @Column(nullable = false)
    private Long nowDonation;

    @Column(nullable = false)
    private Long targetDonation;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate finishDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private Set<BoardImage> boardImageList = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private Set<Comment> commentList = new HashSet<>();

    public void plusDonation(Long donation) {

        if (donation > targetDonation - nowDonation) {
            throw new RuntimeException("남은 금액보다 큰 금액은 기부할 수 없습니다");
        }
        this.nowDonation += donation;
    }

    public void minusDonation(Long donation) {
        this.nowDonation -= donation;
    }

    public void plusCheer() {
        this.cheerCount++;
    }

    public void minusCheer() {
        this.cheerCount--;
    }

    public void changeBoard(RequestBoardDto boardDto) {
        this.title = boardDto.getTitle();
        this.content = boardDto.getContent();
        this.targetDonation = boardDto.getTargetDonation();
        this.startDate = boardDto.getStartDate();
        this.finishDate = boardDto.getFinishDate();
        this.category = Category.builder().id(boardDto.getCategoryId()).build();
    }

}
