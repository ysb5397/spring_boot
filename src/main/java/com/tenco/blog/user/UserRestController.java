package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j // 사용시 Logger 자동 선언됨
@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<ApiUtil<UserResponse.JoinDTO>> join(@RequestBody UserRequest.JoinDTO joinDTO) {
        log.info("회원가입 API 호출 - 사용자명 : {}, 이메일 : {}", joinDTO.getUsername(), joinDTO.getEmail());
        joinDTO.validate();
        UserResponse.JoinDTO joinUser = userService.join(joinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiUtil<>(joinUser));
    }
}
