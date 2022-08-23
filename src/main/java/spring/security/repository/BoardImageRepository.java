package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.security.entity.BoardImage;

import java.util.List;
import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Query("select bi.url from BoardImage bi where bi.board.id =:boardId")
    Optional<List<String>> findByBoardId(@Param("boardId") Long boardId);

    @Query("delete from BoardImage bi where bi.board.id =:boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
