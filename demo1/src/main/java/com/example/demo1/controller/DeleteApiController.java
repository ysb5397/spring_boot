package com.example.demo1.controller;

import com.example.demo1.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController // 컴포넌트 스캔의 대상 --> IoC의 대상(제어의 역전) --> 스프링 컨테이너 안에 싱글톤 패턴으로 관리 됨
public class DeleteApiController {

    /**
     *  DELETE 요청 처리
     *  웹 브라우저 주소창에서 사용이 불가능
     *  HTTP 요청 메시지에 본문은 없음
     *  주소 설계
     *  http://localhost:8080/delete/100?account=xx은행
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable(name = "userId") String userId,
                                    @RequestParam(name = "account") String account) {

        System.out.println("userId : " + userId);
        System.out.println("account : " + account);

//        ResponseEntity responseEntity = new ResponseEntity();

        // 일반적으로 처리하는 로직의 형태
        // 인증 검사
        // 유효성 검사(클라이언트가 던진 데이터가 타당한지 검증)
        // 프로젝트 기능에 맞는 핵심 로직을 수행 --> 서비스 위임
        // 비즈니스 로직을 수행하고 -> repository로 위임

        UserDTO userDTO = new UserDTO();
        userDTO.setName("홍길동");
        userDTO.setAge(10);
        userDTO.setPhoneNumber("010-1234-1234");
        return ResponseEntity.status(HttpStatus.OK).body("정상");
    }

}
