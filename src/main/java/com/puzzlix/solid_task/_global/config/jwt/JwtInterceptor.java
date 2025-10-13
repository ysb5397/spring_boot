package com.puzzlix.solid_task._global.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = resolveToken(request);

        if(token != null && jwtProvider.validateToken(token))
            return true;

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        return false;
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        // 삼항식으로 성공시 token 반환, 실패시 null 반환
        return StringUtils.hasText(token) && token.startsWith("Bearer ") ?
        token.substring(7) : null;
    }
}
