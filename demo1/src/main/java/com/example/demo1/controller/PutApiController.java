package com.example.demo1.controller;

import com.example.demo1.dto.UserDTO;
import com.example.demo1.dto.UserDTO2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/put")
public class PutApiController {
    /**
     *  주소 설계
     *  Method - PUT
     *  http://localhost:8080/put/demo1
     */

    @PutMapping("/demo1")
    public UserDTO2 put1(@RequestBody UserDTO2 userDTO2) {
        System.out.println(userDTO2.toString());

        // return 타입이 Object 타입 ->
        // MappingJackson2HttpMessageConverter 객체가 문자열(json) 변환해서 던짐
        return userDTO2;
    }

    /**
     *  주소 설계
     *  http://localhost:8080/put/user/1
     *
     */
    @PutMapping("/user/{id}")
    public UserDTO put2(@PathVariable(name = "id") int id, @RequestBody UserDTO userDTO) {
        System.out.println("id : " + id);
        System.out.println("user : " + userDTO);
        return userDTO;
    }
}
