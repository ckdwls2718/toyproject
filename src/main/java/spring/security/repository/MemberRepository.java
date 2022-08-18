package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByNickname(String nickname);
}
