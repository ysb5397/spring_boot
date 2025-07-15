package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Utilities;
import javax.swing.text.html.parser.Entity;

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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiUtil<UserResponse.LoginDTO>> login(@RequestBody UserRequest.LoginDTO loginDTO,
                                                                HttpSession session) {
        log.info("로그인 API 호출 - 사용자명 : {}", loginDTO.getUsername());
        loginDTO.validate();
        UserResponse.LoginDTO loginUser = userService.login(loginDTO);
        session.setAttribute(Define.SESSION_USER, loginUser);
        return ResponseEntity.ok(new ApiUtil<>(loginUser));
    }

    // 회원 정보 조회
    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DetailDTO>> getUserInfo(@PathVariable(name = "id") Long id,
                                                                    HttpSession session) {
        log.info("회원 정보 조회 API 호출 - 사용자 아이디 : {}", id);
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        UserResponse.DetailDTO userDetail = userService.findUserById(id, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(userDetail));
    }

    // 회원 정보 수정
    @PutMapping("/api/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.UpdateDTO>> updateUser(@PathVariable(name = "id") Long id,
                                                                      @RequestBody UserRequest.UpdateDTO updateDTO) {
        log.info("회원 정보 수정 API 호출 - 사용자 아이디 : {}", id);
        updateDTO.validate();
        UserResponse.UpdateDTO updateUser = userService.updateById(id, updateDTO);
        return ResponseEntity.ok(new ApiUtil<>(updateUser));
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<ApiUtil<String>> logout(HttpSession session) {
        log.info("로그아웃 API 호출");
        session.invalidate();
        return ResponseEntity.ok().body(new ApiUtil<>("로그아웃 성공"));
    }
}
