package spring.security.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import spring.security.entity.Category;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @Rollback(value = false)
    @DisplayName("카테고리 집어넣기")
    public void insert() throws Exception {
        List<String> list = Arrays.asList("봄", "여름", "가을", "겨울");

        for (String s : list) {
            Category category = Category.builder()
                    .name(s)
                    .build();
            categoryRepository.save(category);
        }


    }

}