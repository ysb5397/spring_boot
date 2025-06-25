package com.tenco.blog._core.errors.exception;

// 403 Forbidden - 권한 없음
// RuntimeException 상속
// 발생 예시
// 1. 로그인 했지만, 권한이 없는 자원에 접근
// 2. 관리자 페이지에 일반 사용자 접근

public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }
}
