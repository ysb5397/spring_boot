package com.tenco.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO, HttpServletRequest request) {

        System.out.println("======== 회원가입 =======");
        System.out.println("사용자명 : " + joinDTO.getUsername());
        System.out.println("사용자 이메일 : " + joinDTO.getEmail());

        try {
            // 1. 입력된 데이터 검증
            joinDTO.validate();

            // 2. 사용자명 중복 체크
            User existUser = userRepository.findByUsername(joinDTO.getUsername());

            if (existUser != null) {
                throw new IllegalArgumentException("이미 존재하는 사용자입니다! / 사용자명 : " + joinDTO.getUsername());
            }

            // 3. DTO를 User Object로 변환
            User user = joinDTO.toEntity();

            // 4. User Object를 영속화 처리
            userRepository.save(user);
            return "redirect:/login-form";
        } catch (Exception e) {
            // 검증 실패시 보통 에러 메시지와 함께 다시 form에 전달
            request.setAttribute("errorMessage", "잘못된 요청입니다.");
            return "user/join-form";
        }
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }
}
