package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;

        // JoinDTO 를 User Object 변환 하는 메서드 추가
        // 계층간 데이터 변환을 위해 명확하게 분리
        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }
        // 회원가입시 유효성 검증 메서드
        public void validate() {

            if(username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("사용자 명은 필수야");
            }
            if(password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("비밀번호는 필수야");
            }
            // 간단한 이메일 형식 검증 (정규화 표현식)
            if(email.contains("@") == false) {
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
            }
        }
    }
    
    // 로그인 용 DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        private String username; 
        private String password; 
        
        // 유효성 검사 
        public void validate() {
            if(username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("야 사용자명 입력해");
            }
            if(password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("야 비밀번호 입력해");
            }
        }
        
    }

    // 회원 정보 수정용 DTO
    @Data
    public static class UpdateDTO {
        private String password;
        private String email;
        private String profileImage;

        public void validate() {
            if(password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("비밀번호는 필수야");
            }
            if(password.length() < 4) {
                throw new IllegalArgumentException("비밀번호는 4자 이상이어야 합니다");
            }
            // 간단한 이메일 형식 검증 (정규화 표현식)
            if(email.contains("@") == false) {
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
            }
        }

    }

    @Data
    public static class ProfileImgDTO {
        // file 정보가 다 담겨 있음
        private MultipartFile profileImg;

        public void validate() {
            if (profileImg == null || profileImg.isEmpty()) {
                throw new Exception400("프로필 이미지를 선택해주세요.");
            }

            if (profileImg.getSize() > 20 * 1024 * 1024) {
                throw new Exception400("파일 크기는 20MB 이하여야 합니다.");
            }

            if (profileImg.getContentType() == null || !profileImg.getContentType().startsWith("image/")) {
                throw new Exception400("이미지 파일만 업로드 가능합니다.");
            }
        }
    }
}
