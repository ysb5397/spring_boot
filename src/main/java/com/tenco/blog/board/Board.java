package com.tenco.blog.board;


import com.tenco.blog.utils.DateUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
// 기본 생성자 - JPA에서 엔티티는 기본 생성자가 필요
@Data
// @Table : 실제 데이터베이스 테이블 명을 지정할 때 사용
@Table(name = "board_tb")
// @Entity : JPA가 이 클래스를 테이터베이스 테이블과 매핑하는 객체(엔티티)로 인식
// 즉, @Entity 어노테이션이 있어야 JPA가 이 객체를 관리 한다.
@Entity
public class Board {

    // @Id 이 필드가 기본키(Primary key) 임을 나타냄
    @Id
    // DENTITY 전략 : 데이터베이스의 기본 전략을 사용한다. -> Auto_Increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 별도 어노테이션이 없으면 필드명이 컬럼명이 됨
    private String title;
    private String content;
    private String username;
    // 하이버 네이트가 제공하는 Annotation
    // 엔티티가 하나 생성이 되면 현재 시간을 자동으로 설정한다
    // pc -> db(날짜 주입)
    // v1 버전에서는 sql now()를 직접 사용했지만 JPA가 자동 처리
    @CreationTimestamp
    private Timestamp createdAt; // created_at (스네이크 케이스로 자동 변환)

    // 생성자
    public Board(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
        // id와 createdAt은 JPA와 하이버 네이트가 자동으로 설정
    }

    // mustache 에서 표현할 time format 기능 구현
    public String getTime() {
        return DateUtil.timestampFormat(createdAt);
    }
}
