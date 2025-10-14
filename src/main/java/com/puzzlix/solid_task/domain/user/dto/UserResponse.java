package com.puzzlix.solid_task.domain.user.dto;

import com.puzzlix.solid_task.domain.user.User;
import lombok.Data;

public class UserResponse {

    @Data
    public static class Detail {
        private final Long id;
        private final String name;
        private final String email;

        public Detail(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
        }
    }
}
