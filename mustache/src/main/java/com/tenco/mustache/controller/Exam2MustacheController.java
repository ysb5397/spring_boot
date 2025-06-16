package com.tenco.mustache.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mustache 문법 학습을 위한 단계별 예제
 */
@Controller
@RequestMapping("/mustache") // 대문 달기 - 각 메서드에서 공통 사용
public class Exam2MustacheController {

    /**
     * 1. 기본 변수 출력 ({{key}}) 학습
     * URL: http://localhost:8080/mustache/basic-variables
     */
    @GetMapping("/basic-variables")
    public String basicVariables(Model model) {
        // 다양한 데이터 타입의 기본 변수들
        model.addAttribute("key", "key");
        model.addAttribute("pageTitle", "기본 변수 출력 학습");
        model.addAttribute("message", "안녕하세요, Mustache입니다!");
        model.addAttribute("userName", "김개발자");
        model.addAttribute("userAge", 28);
        model.addAttribute("userScore", 95.5);
        model.addAttribute("isActive", true);
        model.addAttribute("currentDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return "examples/basic2";
    }

}