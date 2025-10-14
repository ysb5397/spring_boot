package com.puzzlix.solid_task.domain.user;

import com.puzzlix.solid_task._global.config.jwt.JwtProvider;
import com.puzzlix.solid_task._global.dto.CommonResponseDto;
import com.puzzlix.solid_task.domain.user.dto.UserRequest;
import com.puzzlix.solid_task.domain.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserRequest.SignUp request) {
        User newUser = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDto.success(new UserResponse.Detail(newUser)));
    }

    @PostMapping("/login/{type}")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest.Login request, @PathVariable("type") String type, HttpServletResponse response) {
        User loginUser = userService.login(type, request);
        String token = jwtProvider.createToken(loginUser);
        response.setHeader("Authorization", "Bearer " + token);
        return ResponseEntity.ok().body(CommonResponseDto.success(new UserResponse.Detail(loginUser)));
    }
}
