package com.tenco.blog.board;

import com.tenco.blog.reply.Reply;
import com.tenco.blog.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data
    public static class MainDTO {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private String createdAt;

        @Builder
        public MainDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writer = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }

    // 게시글 상세보기 DTO
    @Data
    public static class DetailDTO {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private String createdAt;
        private boolean isBoardOwner;
        private List<ReplyDTO> replies = new ArrayList<>();

        public DetailDTO(Board board, User sessionUser) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writer = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
            this.isBoardOwner = sessionUser != null && board.isOwner(sessionUser.getId());

            for (Reply reply : board.getReplies()) {
                this.replies.add(new ReplyDTO(reply, sessionUser));
            }
        }
    }

    @Data
    public static class ReplyDTO {
        private Long id;
        private String comment;
        private String writer;
        private String createdAt;
        private boolean isReplyOwner;

        public ReplyDTO (Reply reply, User sessionUser) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.writer = reply.getUser().getUsername();
            this.createdAt = reply.getCreatedAt().toString();
            this.isReplyOwner = sessionUser != null && reply.isOwner(sessionUser.getId());
        }
    }

    // 게시글 작성 응답 DTO
    @Data
    public static class SaveDTO {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private String createdAt;

        @Builder
        public SaveDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writer = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }

    // 게시글 수정 응답 DTO
    @Data
    public static class UpdateDTO {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private String createdAt;

        @Builder
        public UpdateDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writer = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }
}
