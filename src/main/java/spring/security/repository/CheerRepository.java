package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.security.entity.Cheer;

public interface CheerRepository extends JpaRepository<Cheer, Long> {

    //JPQL은 select exist를 지원하지 않는다
    boolean existsByMemberEmailAndBoardId(String email, Long boardId);

    @Modifying
    void deleteByMemberEmailAndBoardId(String email, Long boardId);
}
