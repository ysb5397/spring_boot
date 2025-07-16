package com.tenco.blog.reply;

import lombok.Builder;
import lombok.Data;

public class ReplyResponse {

    @Data
    public static class SaveDTO {
        private long id;
        private String comment;
        private String writer;
        private String createdAt;
        private Long boardId;

        @Builder
        public SaveDTO(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.writer = reply.getUser().getUsername();
            this.createdAt = reply.getCreatedAt().toString();
            this.boardId = reply.getBoard().getId();
        }
    }
}
