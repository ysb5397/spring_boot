package com.tenco.blog._core.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tenco.blog._core.errors.exception.Exception401;
import com.tenco.blog._core.errors.exception.Exception500;
import com.tenco.blog._core.utils.Define;
import com.tenco.blog._core.utils.JwtUtil;
import com.tenco.blog.user.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component // Ioc 대상 (싱글톤 패턴으로 관리)
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * preHandle - 컨트롤러에 들어 가지 전에 동작 하는 메서드이다.
     * 리턴타입이 boolean 이라서 true ---> 컨트롤러 안으로 들어간다, false --> 못 들어 감
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("==== JWT 인증 인터셉터 시작 ====");
        String jwt = request.getHeader("Authorization");
        // "Bearer " + 암호화된 토큰
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            throw new Exception401("JMT 토큰 인증이 되지 않은 사용자입니다.");
        }

        jwt = jwt.replace("Bearer ", "");

        try {
            SessionUser sessionUser = JwtUtil.verify(jwt);

            // 구분 중요
            // HttpSession session = request.getSession();
            // session.setAttribute(Define.SESSION_USER, sessionUser);

            // JWT는 stateless를 지키기 위해서 나옴(애초에 모바일은 쿠키에 접근 못함)
            // request.setAttribute는 요청 단위로 데이터를 저장하고 소멸
            // 즉, 해당 데이터는 요청이 처리된 후 사라지며, 서버 세션 메모리에 저장되지 않는다.
            request.setAttribute(Define.SESSION_USER, sessionUser);
            return true;
        } catch (TokenExpiredException e) {
            throw new Exception401("토큰이 만료되었습니다. 다시 로그인해주세요.");
        } catch (JWTDecodeException e) {
            throw new Exception401("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            throw new Exception500(e.getMessage());
        }
    }
}
