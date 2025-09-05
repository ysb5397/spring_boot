package com.example.class_step_websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker // stomp 메시지 브로커 기능 활성화
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    // MessageBroker 설정
    // 브로커는 메시지를 받아서 구독자들에게 배포하는 중간 관리자
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic -> 다수의 클라이언트에게 방송하는 채널
        // /user -> 특정 유저에게 메시지를 보내는 채널
        registry.enableSimpleBroker("/topic", "/user");

        // 클라이언트가 우리쪽으로 보내는 형식 설정
        registry.setApplicationDestinationPrefixes("/app");
    }

    // stomp 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // withSockJs -> 선택적으로 설정, 브라우저 호환성을 위해 설정함
        // 폴링, SSE, JSONP 등으로 알아서 대체
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
