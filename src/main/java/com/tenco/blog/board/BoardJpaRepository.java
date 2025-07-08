package com.tenco.blog.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 *  게시글 관련 데이터베이스 접근을 담당
 *  기본적인 CRUD를 제공
 */
// @Repository 생략 가능
public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    // 기본 CRUD 기능, 다만 추가적인 기능은 직접 선언해주어야 한다.

    // 게시글과 사용자의 정보가 포함된 엔티티를 만들어 주어야 함. (리스트용)
    @Query("select b from Board b join fetch b.user u order by b.id desc ")
    Page<Board> findAllJoinUser(Pageable pageable);
//    List<Board> findAllJoinUser();
    // Join fetch는 모든 Board 엔티티와 연관된 User를 한방 쿼리로 가져옴
    // LAZY 전략이라서 N + 1 방지를 할 수 있다.
    // N + 1 문제 : 게시글 11개가 있다면, 지연 로딩 1(Board 조회) + 11(User 조회) = 12번 쿼리가 발생

    // 게시글 ID로 한방에 유저 정보도 가져오기 - JOIN Fetch 사용하면 끝
    @Query("select b from Board b join fetch b.user u where b.id = :id ")
    Optional<Board> findByIdJoinUser(@Param("id") Long id);

}
