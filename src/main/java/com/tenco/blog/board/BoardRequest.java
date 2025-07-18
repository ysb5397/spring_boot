package com.tenco.blog.board;

import com.tenco.blog.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

public class BoardRequest {

    // 게시글 저장 DTO
    @Data
    public static class SaveDTO {
        @NotEmpty(message = "제목은 필수입니다.")
        private String title;

        @NotEmpty(message = "내용은 필수입니다.")
        private String content;

        public Board toEntity(User user) {
            return Board.builder()
                    .title(this.title)
                    .user(user)
                    .content(this.content)
                    .build();
        }
    }

    // 게시글 수정용 DTO 설계
    @Data
    public static class UpdateDTO {

        @NotEmpty(message = "제목은 필수입니다.")
        private String title;

        @NotEmpty(message = "내용은 필수입니다.")
        private String content;
    }
}
