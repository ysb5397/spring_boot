package com.tenco.blog.user;

import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    /**
     * 회원가입 기능 화면 요청
     * @return
     */
    @GetMapping("/join-form")
    public String joinForm() {
        log.info("회원가입 요청 폼");
        return "user/join-form";
    }

    /**
     * 회원가입 기능 요청
     * @param joinDTO
     * @return
     */
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO) {

        joinDTO.validate();
        userService.join(joinDTO);
        return "redirect:/login-form";

    }

    /**
     * 로그인 기능 화면 요청
     *
     * @return login-form.mustache
     */
    @GetMapping("/login-form")
    public String loginForm() {
        log.info("로그인 요청 폼");
        return "user/login-form";
    }

    /**
     * 로그인 기능 요청
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO, HttpSession session) {
        loginDTO.validate();
        User user = userService.login(loginDTO);
        session.setAttribute(Define.SESSION_USER, user);
        return "redirect:/";
    }

    // 로그아웃 처리
    // 아래 코드 주석으로 설명
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("로그아웃 시도");
        session.invalidate();
        return "redirect:/";
    }

    /**
     * 회원 정보 수정 화면 요청
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/user/update-form")
    public String updateForm(Model model, HttpSession session) {

        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        User user = userService.findById(sessionUser.getId());
        
        model.addAttribute("user", user);
        return "user/update-form";
    }

    /**
     * 회원 정보 수정 요청
     * @param updateDTO
     * @param session
     * @return
     */
    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO, HttpSession session) {

        // 1. 인증검사
        // 2. 유효성 검사
        // 3. 서비스 계층 -> 회원 수정 기능 위임
        // 4. 세션 동기화
        // 5. 리다이렉트
        
        updateDTO.validate();
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        User updateUser = userService.updateById(sessionUser.getId(), updateDTO);
        session.setAttribute(Define.SESSION_USER, updateUser);

        // 다시 회원 정보 보기
        return "redirect:/user/update-form";
    }
}
