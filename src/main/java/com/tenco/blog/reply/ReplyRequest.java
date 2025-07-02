package com.tenco.blog.reply;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog.board.Board;
import com.tenco.blog.user.User;
import lombok.Builder;
import lombok.Data;

public class ReplyRequest {

    @Data
    public static class SaveDTO {
        private Long boardId; // 게시글 id
        private String comment;

        /**
         * DI로 멤버 변수에 없는걸 주입
         */
        public Reply toEntity(User sessionUser, Board board) {
            return Reply.builder()
                    .comment(this.comment.trim())
                    .user(sessionUser)
                    .board(board)
                    .build();
        }

        public void validate() {
            if (comment == null || comment.trim().isEmpty()) {
                throw new Exception400("댓글은 필수입니다");
            }

            if (comment.length() > 500) {
                throw new Exception400("댓글은 500자 이내로 입력해주세요.");
            }

            if (boardId == null) {
                throw new Exception404("게시글이 없습니다.");
            }
        }
    }
}
