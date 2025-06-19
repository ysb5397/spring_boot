package com.tenco.blog.repository;

import com.tenco.blog.model.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

// @Import - 테스트에 사용할 수 있도록 해당 클래스를 로드한다
@Import(BoardNativeRepository.class)
@DataJpaTest // JPA 관련 테스트만 로드하는 테스트
public class BoardNativeRepositoryTest {

    @Autowired // 자동으로 의존성 주입
    private BoardNativeRepository br;

    // DI - 의존 주입
    /// public BoardNativeRepositoryTest(BoardNativeRepository br) {
    //    this.br = br;
    //}

    @Test
    public void findAll_test() {
        // given -- 테스트를 위한 준비 단계
        // 게시글 목록 조회가 정상 작동 하는지 확인 --> sample data(data.sql)

        // when -- 실제 테스트를 하는 행위(전체 게시글 목록 조회)
        List<Board> boardList = br.findAll();

        // then -- 결과 검증(예상하는대로 동작하는지 검증)
        Assertions.assertThat(boardList.size()).isEqualTo(4);
        Assertions.assertThat(boardList.get(3).getUsername()).isEqualTo("ssar");
    }

    @Test
    public void findById_test() {
        int id = 1;
        Board board = br.findById(id);

        Assertions.assertThat(board.getTitle()).isEqualTo("제목1");
        Assertions.assertThat(board.getUsername()).isEqualTo("ssar");

        // 객체가 null이 아닌지 확인
        Assertions.assertThat(board).isNotNull();
    }
}
