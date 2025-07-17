package com.tenco.blog.user;

import lombok.Builder;
import lombok.Data;

public class UserResponse {

    @Data
    public static class JoinDTO {
        private Long id;
        private String username;
        private String email;
        private String createdAt;

        @Builder
        public JoinDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt().toString();
        }
    }

    @Data
    public static class LoginDTO {
        private Long id;
        private String username;
        private String email;

        @Builder
        public LoginDTO (User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }

    @Data
    public static class UpdateDTO {
        private Long id;
        private String username;
        private String email;

        @Builder
        public UpdateDTO (User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }

    @Data
    public static class DetailDTO {
        private Long id;
        private String username;
        private String email;

        @Builder
        public DetailDTO (User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }
}
