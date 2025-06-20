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

    @Test
    public void update_test() {
        int id = 1;
        // update --> 더티 체킹
        Board board = br.findById(id);
        System.out.println("username: " + board.getUsername());

        board.setUsername("미상");
        // 영속성 컨텍스트에서 관리되는 상태이므로 굳이 br.update를 하지 않아도 동기화가 된다
//        br.update(board);
//        board = br.findById(id);

        // 업데이트 완료 됨 -> 미상으로
        Assertions.assertThat(board.getUsername()).isEqualTo("미상");
    }

    @Test
    public void delete_test() {
        int id = 1;
        Board board = br.findById(id);
        System.out.println("title: " + board.getTitle());

        Assertions.assertThat(board).isNotNull();
        Assertions.assertThat(board.getTitle()).isEqualTo("제목1");

        br.delete(board);

        board = br.findById(id);
        Assertions.assertThat(board).isNull();
    }
}
