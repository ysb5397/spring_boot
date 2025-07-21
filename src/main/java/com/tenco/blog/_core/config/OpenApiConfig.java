package com.tenco.blog._core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 기본 설정하는 클래스 API 문서의 제목, 설명, JWT 인증 설정
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tenco Blog API")
                        .description("RESTful API")
                        .version("1.0")) // API 기본 정보
                .components(new Components()
                        .addSecuritySchemes("jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("jwt")))

                // 모든 API에 jwt 인증 적용
                .addSecurityItem(new SecurityRequirement()
                        .addList("jwt"));
    }
}
