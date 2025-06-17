package com.example.demo1.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

/**
 *  GET 방식 요청 주소 설계와 핸들러 처리를 학습
 */
// IoC의 대상 -- 스프링 프레임 워크가 자동으로 new 해줌
// @Controller --> 스프링 프레임 워크 안에 view resolve -> 해당 경로에 파일 찾는 일을 함
// @RestController --> 데이터를 반환
@Controller // @Controller + @ResponseBody1
public class GetApiController {

    // GET 방식으로 요청을 하면 처리하는 메서드를 만들어야 한다
    // 주소 설계: http://localhost:8080/hello

    @GetMapping("/hello")
    @ResponseBody // 이걸 추가하면 파일 경로를 찾는게 아니라 그냥 데이터를 반환 하라는 뜻
    public String hello() {
        // index.mustache
        return "index";
    }

    /**
     *  쿼리스트링 방식 (@RequestParam)
     *  주소 설계
     *  http://localhost:8080/qs1?name=둘리
     * @param name=value
     * @return String
     */
    @GetMapping("/qs1")
    public String qs1(@RequestParam(name = "name") String name) {
        return "응답받은 name 키 값의 value 값은 = " + name;
    }

    /**
     *  쿼리 스트링 방식
     *  주소 설계
     *  http://localhost:8080/qs1?name=둘리&age=10
     *  required = false, defaultValue = "0"
     */
    @GetMapping("/qs2")
    public String qs2(@RequestParam(name = "name") String name, @RequestParam(name = "age", required = false, defaultValue = "0") int age) {
        System.out.println("name = " + name);
        System.out.println("age = " + age);
        return "name 값은 = " + name + " / age 값은 = " + age;
    }

    @GetMapping("/qs3")
    @ResponseBody
    public String qs3(@RequestParam(name = "date") String date) {
        int year;
        int month;
        int day;

        year = Integer.parseInt(date.substring(0, 4));
        month = Integer.parseInt(date.substring(4, 6));
        day = Integer.parseInt(date.substring(6));

        LocalDate dateTemp = LocalDate.of(year, month, day);
        System.out.println("dateTemp = " + dateTemp);

        return "현재 날짜 : " + date;
    }

    @GetMapping("/qs4")
    @ResponseBody
    public User helloObject() {
        // 응답시에 데이터를 반환, 단 User Object로 반환
        // Object (서버측) 응답 시킬 때 -- 잭슨, GSON
        // 문자열 변환을 위해선 Getter가 필수
        return new User("test", 10);
    }

    @AllArgsConstructor
    @Getter
    class User {
        private String name;
        private int age;
    }

    @GetMapping("/qs5")
    @ResponseBody
    public String qs5(@RequestParam Map<String, String> data) {
        // Map 방식으로 동적으로 들어오는 키와 값을 받아서 처리해 보자.
        StringBuffer sb = new StringBuffer();
        data.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " = " + entry.getValue());
            sb.append(entry.getKey() + " = " + entry.getValue() + "\n");
        });

        return sb.toString();
    }
}
