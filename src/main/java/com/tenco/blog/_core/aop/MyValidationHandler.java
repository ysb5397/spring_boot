package com.tenco.blog._core.aop;

import com.tenco.blog._core.errors.exception.Exception400;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * AOP를 활용한 유효성 검사 자동화 처리
 * POST, PUT 요청에서 발생하는 유효성 검사를 자동으로 처리
 */
@Aspect
@Component
@Slf4j
public class MyValidationHandler {

    /**
     * 동작 원리
     * 1. POST, PUT 요청이 Controller 메서드에 도달하기 전에 가로채기
     * 2. 메서드의 매개 변수 중 Errors 객체 찾기
     * 3. 유효성 검사 오류가 있으면 400 에러 던지기
     */

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void validation(JoinPoint joinPoint) {

        log.debug("=== AOP 유효성 검사 시작 ===");
        log.debug("실행 메서드 : {}", joinPoint.getSignature().getName());

        // joinPoint에서 메서드의 모든 매개변수 가져오기
        Object[] objects = joinPoint.getArgs();
        for (Object args : objects) {
            if (args instanceof Errors) {
                Errors errors = (Errors) args;

                if (errors.hasErrors()) {
                    log.warn("유효성 검사 오류 발견, 오류 갯수 : {}", errors.getErrorCount());
                    FieldError firstError = errors.getFieldErrors().getFirst();
                    String errorMessage = firstError.getField() + " / " + firstError.getDefaultMessage();
                    throw new Exception400(errorMessage);
                }
                break;
            }
        }
        log.debug("유효성 검사 완료");
    }
}
