package com.tenco.blog.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r join fetch r.board b where b.id = :boardId order by r.id desc")
    List<Reply> findByBoard(@Param("boardId") Long id);

}
