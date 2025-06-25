package com.tenco.blog._core.errors.exception;

// 400 BadRequest - 잘못된 요청
// RuntimeException 상속
// 발생 예시
// 1. 유효성 검사 실패
// 2. 잘못된 파라미터

public class Exception400 extends RuntimeException {

    // 에러 메시지로 사용할 문자열을 슈퍼 클래스로 전달
    public Exception400(String message) {
        super(message);
    }
}
