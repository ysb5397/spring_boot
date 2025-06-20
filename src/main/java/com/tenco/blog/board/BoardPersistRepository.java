package com.tenco.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // 필수 멤버변수를 확인해서 생성자에 등록해줌
@Repository // IoC 대상 - 싱글톤 패턴
public class BoardPersistRepository {

    // JPA 핵심 인터페이스
    // 의존 주입
    // final 을 쓰면 @Autowired을 못씀 --> final을 성능 향상을 위해 씀
    private final EntityManager em;

    // 게시글 저장 기능 -- 영속성 컨텍스트 활용
    @Transactional
    public Board save(Board board) {
        // v1 -> NativeQuery
        // 1. 매개 변수로 받은 board는 현재 비영속 상태
        // --> 아직 영속성 컨텍스트에 관리 되지 않는 상태
        // --> 데이터베이스와 아직 연관 없는 순수 java 객체 상태

        // 2. em.persist(board); -> 이 엔티티를 영속성 컨텍스트에 저장하는 개념이다.
        // --> 영속성 컨텍스트가 board 객체를 관리하게 한다
        em.persist(board);

        // 3. 트랜잭션 커밋 시점에 insert 쿼리 실행
        // --> 이때, 영속성 컨텍스트의 변경 사항이 자동으로 db에 반영됨
        // --> board 객체의 id 필드에 자동으로 생성된 값이 설정 됨

        // 4. 영속 상태로 된 board 객체를 반환
        // --> 이 시점에는 자동으로 board id 멤버 변수에 db pk 값이 할당된 상태
        return board;
    }

    // 게시글 조회 기능(JPQL)
    public List<Board> findAll() {
        // JPQL : 엔티티 객체를 대상으로 하는 객체지향 쿼리
        // Board는 엔티티명, b는 별칭
        String jpql = "select b from Board b order by b.id desc ";
//        Query query = em.createQuery(jpql, Board.class);
//        List<Board> boardList = query.getResultList();
        // select b from Board b order by b.id desc = select * from

        return em.createQuery(jpql, Board.class).getResultList();
    }

    // 게시글 선택 조회 기능
    public Board findById(int id) {
//        Board board = em.find(Board.class, id);
        return em.find(Board.class, id);
    }

    // 비교용 -> JPQL 방식으로 게시글 선택 조회
    public Board findByIdWithJPQL(int id) {
        String jpql = "select b from Board b where b.id = :id ";
        // ? 대신 :변수명 으로 설정 후
        // setParameter에서 "변수명", 값 으로 넣어준다.(이때, : 콜론 기호는 절대 장식용이 아니다)

        try {
            return em.createQuery(jpql, Board.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    // JPQL 단점
    // 1. 1차 캐시를 우회하여 항상 DB 접근
    // 2. 코드 복잡 우려
    // 3. getSingleResult 호출 시 예외처리

    // 업데이트 구문
    @Transactional
    public void update(Board board) {
        String jpql = "update Board b set b.title = :title, b.content = :content, b.username = :username where b.id = :id ";

        Query query = em.createQuery(jpql);

        query.setParameter("title", board.getTitle());
        query.setParameter("content", board.getContent());
        query.setParameter("username", board.getUsername());
        query.setParameter("id", board.getId());
        query.executeUpdate();
    }
}
