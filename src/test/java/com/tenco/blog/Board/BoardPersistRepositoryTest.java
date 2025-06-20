package com.tenco.blog.Board;

import com.tenco.blog.board.Board;
import com.tenco.blog.board.BoardPersistRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(BoardPersistRepository.class)
@DataJpaTest
public class BoardPersistRepositoryTest {

    @Autowired
    private BoardPersistRepository br;

    @Test
    public void save_test() {
        // given, when, then
        Board board = new Board("제목", "내용", "사용자");

        // 저장전 상태값 확인
        Assertions.assertThat(board.getId()).isNull();
        System.out.println("저장 전 DB의 board: " + board);

        // 영속성 컨텍스트를 통한 엔티티 저장
        Board saveBoard = br.save(board);

        // 저장 후에 자동 생성된 id 확인
        Assertions.assertThat(saveBoard.getId()).isNotNull();
        Assertions.assertThat(saveBoard.getId()).isGreaterThan(0);

        // 제목 일치 여부 확인
        Assertions.assertThat(saveBoard.getTitle()).isEqualTo("제목");

        // JPA 가 자동으로 생성한 생성 시간 확인
        Assertions.assertThat(saveBoard.getCreatedAt()).isNotNull();

        // 원본 객체 - board, 영속성 컨텍스트에 저장한 saveBoard
        Assertions.assertThat(board).isSameAs(saveBoard);

    }

    @Test
    public void findAll_test() {
        List<Board> boardList = br.findAll();

        System.out.println("size 테스트: " + boardList.size());
        System.out.println("첫번째 게시글 제목 확인: " + boardList.get(0).getTitle());

        // 네이티브 쿼리를 사용한다는 것은 영속 컨텍스트를 우회 해서 일 처리
        // JPQL 바로 영속성 컨텍스트를 우회하지만 조회된 이후에는 영속성 상태가 된다.
        for (Board b : boardList) {
            Assertions.assertThat(b.getId()).isNotNull();
        }
    }

    @Test
    public void findById_test() {
        int id = 1;

        Board board = br.findById(id);
        Assertions.assertThat(board.getTitle()).isEqualTo("제목1");
    }
}
