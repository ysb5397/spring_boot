package com.example.class_step_websocket.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatWebsocketHandler implements WebSocketHandler {
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final ChatService chatService;

    // WebsocketSession -> 개별 클라이언트와 Websocket 연결을 나타내는 객체
    // 각 클라이언트마다 고유한 세션 ID를 가지고 있음
    // 메시지 전송, 연결 상태 확인, 속성 저장 등의 기능을 제공
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionMap.put(session.getId(), session);
        log.debug("웹 소켓 연결 성공");

        session.sendMessage(new TextMessage("서버에 연결이 되었습니다."));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();
        log.debug("메시지 수신됨 : {}", payload);

        // 메시지 형식 처리
        // 메시지 프로토콜 정의
        // 클라이언트에서 CHAT: 이라고 던진다면
        if (payload.startsWith("CHAT:")) {
            String chatMessage = payload.substring(5);
            chatService.saveChat(chatMessage);

            // 브로드 캐스트
            broadcastMessage(chatMessage);
        } else {
            // 알 수 없는 메시지 프로토콜
            session.sendMessage(new TextMessage("ERROR:알 수 없는 메시지 형식입니다"));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("웹 소켓 전송 에러 : {}", exception);
        sessionMap.remove(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionMap.remove(session.getId());
    }

    // 부문 메시지 지원 여부
    // 큰 메시지를 조각으로 나누어 전송하는 방식
    // 채팅 메시지는 보통 짧은 텍스트만 처리하기 때문에 false로 반환
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 연결된 모든 클라이언트에게 메시지를 보내주는 기능
    private void broadcastMessage(String message) {
        sessionMap.entrySet().removeIf(entry -> {
            try {
                entry.getValue().sendMessage(new TextMessage("MESSAGE:" + message));
                return false;
            } catch (Exception e) {
                log.error("오류 발생");
                return true;
            }
        });
    }
}
