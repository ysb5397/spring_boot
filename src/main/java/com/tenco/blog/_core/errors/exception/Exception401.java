package com.tenco.blog._core.errors.exception;

// 401 Unauthorized - 인증되지 않은 사용자
// RuntimeException 상속
// 발생 예시
// 1. 로그인 하지 않은 사용자 접근
// 2. 유효하지 않은 토큰

public class Exception401 extends RuntimeException {
    public Exception401(String message) {
        super(message);
    }
}
