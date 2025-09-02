package com.example.class_step_websocket.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseService {

    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 시간 설정
    private static final long TIMEOUT = 5 * 60 * 1000L;

    // 새로운 SSE 연결 생성 기능
    public SseEmitter addEmitter(String clientId) {
        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);

        emitters.put(clientId, sseEmitter);
        log.info("새로운 연결 요청 - SSE 객체 생성");

        sseEmitter.onCompletion(() -> log.info("연결 요청 완료"));
        sseEmitter.onTimeout(() -> emitters.remove(clientId));
        sseEmitter.onError((e) -> emitters.remove(clientId));

        try {
            sseEmitter.send(SseEmitter.event().name("connect").data("연결 성공"));
        } catch (Exception e) {
            log.error("초기 메시지 전송 실패 : {}", clientId);
        }

        return sseEmitter;
    }

    public void broadcast(String message) {
        emitters.forEach(((clientId, sseEmitter) -> {
            try {
                sseEmitter.send(SseEmitter.event().name("broadcast").data(message));
            } catch (IOException e) {
                log.error("메시지 방송 중 오류");
                emitters.remove(clientId);
            }
        }));
    }
}
