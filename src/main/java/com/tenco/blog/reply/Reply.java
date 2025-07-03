package com.tenco.blog.reply;

import com.tenco.blog.board.Board;
import com.tenco.blog.user.User;
import com.tenco.blog.utils.DateUtil;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Table(name = "reply_tb")
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String comment;

    // 한명의 사용자가 여러 댓글을 달 수 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 하나의 게시글에 댓글이 달릴 수 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Reply(Long id, String comment, User user, Board board, Timestamp createdAt) {
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.board = board;
        this.createdAt = createdAt;
    }

    // 데이터 베이스에 생성이 안되는 필드(변수)
    // -> 뷰에 현재 로그인한 사용자가 여러개의 댓글 중 내가 작성한
    // 댓글의 삭제 기능을 추가하기 위해 편의성 변수를 할당
    @Transient
    private boolean isReplyOwner;

    public boolean isOwner(Long sessionId) {
        return this.user.getId().equals(sessionId);
    }

    public String getTime() {
        return DateUtil.timestampFormat(createdAt);
    }
}
