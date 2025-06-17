package com.example.demo1.controller;

import com.example.demo1.dto.UserDTO;
import com.example.demo1.dto.Users;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // IoC의 대상이 됨
@RequestMapping("/post") // 컨트롤러 객체의 대문 (공통 사용)
public class PostApiController {
    /**
     *  주소 설계
     *  POST 방식
     *  http://localhost:8080/post/demo1
     *  http body = {"name" : "테스트", "age" : 10}
     */
    @PostMapping("/demo1")
    public String demo1(@RequestBody Map<String, Object> reqData) {
        StringBuffer sb = new StringBuffer();
        reqData.entrySet().forEach(e -> {
            System.out.println("key : " + e.getKey() + ", value : " + e.getValue());
            sb.append(e.getKey() + " : " + e.getValue() + "\n");
        });
        return sb.toString();
    }

    /**
     *  주소 설계
     *  POST 방식
     *  http://localhost:8080/post/demo2
     *  http body = {"name" : "테스트", "age" : 10}
     *  [POST는 요청에 본문이 있음 - Object 파싱]
     */
    @PostMapping("/demo2")
    // public String demo2(@RequestBody UserDTO userDTO)
    public UserDTO demo2(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        System.out.println(userDTO.getName());
        System.out.println(userDTO.getAge());
        System.out.println(userDTO.getPhoneNumber());

        return userDTO;
    }

    @PostMapping("/demo3")
    // public String demo2(@RequestBody UserDTO userDTO)
    public Users demo3(@RequestBody Users dto) {
        System.out.println(dto.toString());

        return dto;
    }
}
