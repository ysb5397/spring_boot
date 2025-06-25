package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception401;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    // HttpSession --> 세션 메모리에 접근을 할 수 있음
    private final HttpSession httpSession;

    @GetMapping("/join-form")
    public String joinForm() {
        log.info("회원가입 요청 폼");
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO, HttpServletRequest request) {

        log.info("회원가입 기능 요청");
        log.info("======== 회원가입 =======");
        log.info("사용자명 : {}", joinDTO.getUsername());
        log.info("사용자 이메일 : {}", joinDTO.getEmail());

        // 1. 입력된 데이터 검증
        joinDTO.validate();

        // 2. 사용자명 중복 체크
        User existUser = userRepository.findByUsername(joinDTO.getUsername());

        if (existUser != null) {
            throw new Exception400("이미 존재하는 사용자입니다! / 사용자명 : " + joinDTO.getUsername());
        }

        // 3. DTO를 User Object로 변환
        User user = joinDTO.toEntity();

        // 4. User Object를 영속화 처리
        userRepository.save(user);
        return "redirect:/login-form";

    }

    /**
     * 로그인 화면 요청
     *
     * @return login-form.mustache
     */
    @GetMapping("/login-form")
    public String loginForm() {
        log.info("로그인 요청 폼");
        return "user/login-form";
    }

    // 로그인 액션 처리
    // 자원의 요청은 GET 방식(단, 로그인은 예외)
    // 보안상의 이유

    // DTO 패턴 활용
    // 1. 입력 데이터 검증
    // 2. 사용자명과 비밀번호를 DB에 접근해서 조회
    // 3. 로그인 성공과 실패 처리
    // 4. 로그인 성공이라면 서버측 메모리에 사용자 정보를 저장
    // 5. 메인 화면으로 리다이렉트 처리
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO) {
        log.info("===== 로그인 시도 =====");
        log.info("사용자명: {}", loginDTO.getUsername());

        // 1.
        loginDTO.validate();
        // 2.
        User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());

        // 3.
        if (user == null) {
            // 로그인 실패 : 일치하는 사용자 없음
            throw new Exception400("사용자명 또는 비밀번호가 일치하지 않습니다");
        }

        // 4.
        httpSession.setAttribute("sessionUser", user);

        // 5. 로그인 성공 후 리스트 페이지 이동
        return "redirect:/";

    }

    // 로그아웃 처리
    // 아래 코드 주석으로 설명
    @GetMapping("/logout")
    public String logout() {
        log.info("로그아웃 시도");
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user/update-form")
    public String updateForm(HttpServletRequest request, HttpSession session) {

        log.info("회원 정보 수정 폼 요청");
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new Exception401("로그인이 필요한 서비스입니다.");
        }

        request.setAttribute("user", sessionUser);

        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO, HttpSession session) {

        log.info("회원 정보 수정 요청");

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            throw new Exception401("로그인이 필요한 서비스입니다.");
        }

        updateDTO.validate();

        // 회원정보 수정은 권한 체크가 필요 없음(세션에서 정보를 가져오기 때문)
        User updateUser = userRepository.updateById(sessionUser.getId(), updateDTO);
        session.setAttribute("sessionUser", updateUser);

        // 다시 회원 정보 보기
        return "redirect:/user/update-form";
    }
}
