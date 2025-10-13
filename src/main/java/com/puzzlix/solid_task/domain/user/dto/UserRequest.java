package com.puzzlix.solid_task.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class UserRequest {

    @Data
    public static class SignUp {
        @NotBlank
        @Size(min = 2, max = 20)
        private String name;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 4, max = 20)
        private String password;
    }

    @Data
    public static class Login {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 4, max = 20)
        private String password;
    }
}
