package com.tenco.blog.user;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog._core.errors.exception.Exception401;
import com.tenco.blog._core.utils.Define;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    // 회원가입 API
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserRequest.JoinDTO joinDTO,
                                  Errors errors) {

        UserResponse.JoinDTO joinUser = userService.join(joinDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiUtil<>(joinUser));
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest.LoginDTO loginDTO,
                                   Errors errors) {

        String jwt = userService.login(loginDTO);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .body(new ApiUtil<>(null));
    }

    // 회원 정보 조회 API
    @GetMapping("/api/users/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable(name = "id") Long id,
                                         @RequestAttribute(Define.SESSION_USER) SessionUser sessionUser) {

        // 인증 체크
        if (sessionUser == null) {
            throw new Exception401("인증정보가 없습니다.");
        }
        UserResponse.DetailDTO userDetail = userService.findUserById(id, sessionUser.getId());
        return ResponseEntity.ok().body(new ApiUtil<>(userDetail));
    }

    // 회원 정보 수정 API
    @PutMapping("api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long id,
                                        @Valid @RequestBody UserRequest.UpdateDTO updateDTO,
                                        Errors errors,
                                        @RequestAttribute(Define.SESSION_USER) SessionUser sessionUser) {

        // 인증 체크
        if (sessionUser == null) {
            throw new Exception401("인증정보가 없습니다.");
        }

        UserResponse.UpdateDTO updateUser = userService.updateById(id, sessionUser.getId(), updateDTO);
        return ResponseEntity.ok().body(new ApiUtil<>(updateUser));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body(new ApiUtil<>("로그아웃 완료"));
    }
}
