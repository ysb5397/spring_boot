package com.tenco.blog._core.config;

import com.tenco.blog._core.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration // IoC 처리 (싱글톤 패턴 관리)
public class WebMvcConfig implements WebMvcConfigurer {

    // DI 처리(생성자 의존 주입)
    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                // 공개 API는 예외 처리
                .excludePathPatterns(
                        "/api/boards", // 게시글 목록은 누구나 응답 받을 수 있음
                        "/api/boards/{id:\\d+}", // 게시글 상세보기
                        "/api/auth/login",
                        "/api/auth/join"
                );
    }

    // cors 정책설정(중복 등록 가능)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api-test/**")
//                .allowedOrigins("https://api.kakao.com:8080")
                .allowedOrigins("*") // 허용되는 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용되는 method
                .allowedHeaders("*") // 허용되는 header
                .allowCredentials(false); // 인증이 필요한 경우 true

        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
