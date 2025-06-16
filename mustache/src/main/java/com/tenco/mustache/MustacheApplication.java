package com.tenco.mustache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  @SpringBootApplication : 스프링 부트 자동 설정 활성화
 *
 *  컴포넌트 스캔의 시작점
 *  내장 톰캣 서버 설정
 */
@SpringBootApplication
public class MustacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(MustacheApplication.class, args);
	}

}
