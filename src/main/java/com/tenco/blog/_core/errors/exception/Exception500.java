package com.tenco.blog._core.errors.exception;

// 500 Internal Server Error - 서버 에러
// RuntimeException 상속
// 발생 예시
// 1. DB 연결 실패
// 2. 서버에서 알 수 없는 에러 발생

public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }
}
