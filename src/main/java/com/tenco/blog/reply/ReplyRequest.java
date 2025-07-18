package com.tenco.blog.reply;

import com.tenco.blog.board.Board;
import com.tenco.blog.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReplyRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveDTO {
        private Long boardId;

        @NotEmpty(message = "댓글 내용은 필수입니다.")
        private String comment;

        public Reply toEntity(User sessionUser, Board board) {
            return Reply.builder()
                    .comment(comment.trim())
                    .user(sessionUser)
                    .board(board)
                    .build();
        }
    }
}
