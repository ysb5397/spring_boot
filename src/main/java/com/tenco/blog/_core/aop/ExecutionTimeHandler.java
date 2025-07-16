package com.tenco.blog._core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Aspect // AOP - 관점 지향
@Component // IoC 대상 / Bean으로 처리됨
@Slf4j
public class ExecutionTimeHandler {

    /*
     * 요청부터 응답까지의 실행시간을 측정하여 로그에 기록하는 Advice로 지정
     * JoinPoint 지정(특정 시점 정의) -> @Around : 메서드의 실행 전과 후에 동작
     * PointCut - 어떤 메서드가 실행될 때 Advice가 동작할지 지정(표현식 지정 가능 - execution(* com.tenco.blog..*(..)))
     * 어떤 일을 수행해야 하는지 구체적으로 명시
     */

    //@Around("execution(* com.tenco.blog..*(..))")
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // ProceedingJoinPoint --> 중요한 통로 관리자
        // 예시 : 행사를 진행하는 담당자, 행사 진행 전 준비와 행사 진행 후 마무리를 담당
        // *이제 행사 시작* 신호를 주면 행사(메서드)가 진행 됨

        // 1. 요청 시작 시간을 기록
        long startTime = System.currentTimeMillis();

        // 2. 원래 진행하고자 하는 메서드를 시작하는 신호를 보냄
        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Advice 실행 중 오류가 발생했습니다. / {}", e.getMessage());
            throw e;
        }

        // 3. 응답 완료 시간 기록 및 실행 시간 계산
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("{} 메서드 실행 완료, 걸린 시간 : [{}]ms", joinPoint.getSignature().getName(), executionTime);

        return result;
    }
}
