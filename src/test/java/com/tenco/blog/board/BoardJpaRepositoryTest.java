package com.tenco.blog.board;

import com.tenco.blog.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BoardJpaRepositoryTest {

    @Autowired
    private BoardJpaRepository boardJpaRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void save_test() {
        User user = User.builder()
                .username("testUser")
                .password("1234")
                .email("a@naver.com")
                .build();

        em.persist(user);

        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();

        boardJpaRepository.save(board);
    }
}