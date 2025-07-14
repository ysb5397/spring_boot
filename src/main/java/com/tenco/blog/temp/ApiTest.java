package com.tenco.blog.temp;

import com.tenco.blog.user.User;
import com.tenco.blog.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin(origins = "*") 해당 컨트롤러에서 직접 접근을 허용하는 방법
@RestController
@RequiredArgsConstructor
public class ApiTest {

    private final UserService userService;

    @GetMapping("/api-test/user")
    public User getUsers() {
        System.out.println("API 호출!");
        return userService.findById(1L);
    }
}
