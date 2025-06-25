package com.tenco.blog._core.errors.exception;

// 404 Not Found - 페이지를 찾을 수 없음
// RuntimeException 상속
// 발생 예시
// 1. 주소에 오타
// 2. 존재하지 않는 페이지 요청

public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }
}
