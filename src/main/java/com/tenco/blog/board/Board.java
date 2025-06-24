package com.tenco.blog.board;


import com.tenco.blog.user.User;
import com.tenco.blog.utils.DateUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
// 기본 생성자 - JPA에서 엔티티는 기본 생성자가 필요
@Data
@Table(name = "board_tb")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    // V2에서 사용했던 방식
    // private String username;
    // V3 에서 Board 엔티티는 User 엔티티와 연관관계가 성립이 된다

    // 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래키 컬러명 명시
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;

    // 생성자 만들어 주기
//    public Board(String title, String content, String username) {
//        this.title = title;
//        this.content = content;
//        this.username = username;
//    }


    public String getTime() {
        return DateUtil.timestampFormat(createdAt);
    }

}