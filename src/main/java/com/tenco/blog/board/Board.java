package com.tenco.blog.board;


import com.tenco.blog.reply.Reply;
import com.tenco.blog.user.User;
import com.tenco.blog.utils.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "user_id", nullable = false) // 외래키 컬러명 명시
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;

    public String getTime() {
        return DateUtil.timestampFormat(createdAt);
    }

    // 게시글의 소유자를 직접 확인하는 기능을 만들어보자
    public boolean isOwner(Long checkUserId) {
        return this.user.getId().equals(checkUserId);
    }

    /**
     * 게시글과 댓글을 양방향 맵핑으로 설계
     * FK의 주인은 reply
     * mappedBy는 FK의 주인이 아닌 엔티티에 설정
     *
     * CascadeType.REMOVE
     * 영속성 전이
     * 게시글 삭제 시 관련된 모든 댓글도 자동 삭제 처리
     */
    // 일대다
    @OrderBy("id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();
}