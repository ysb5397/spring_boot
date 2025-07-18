package com.tenco.blog.reply;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository // JpaRepository 상속 <--- 선언 불필요
public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

}
