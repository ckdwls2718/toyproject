package spring.security.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CheerRepositoryTest {

    @Autowired
    CheerRepository cheerRepository;

    @Test
    public void save() throws Exception {
        String email = "test@naver.com";

        boolean b = cheerRepository.existsByMemberEmailAndBoardId(email, 12L);
    }

    @Test
    public void delete() throws Exception {
        String email = "test@naver.com";

        cheerRepository.deleteByMemberEmailAndBoardId(email, 12L);


    }

}