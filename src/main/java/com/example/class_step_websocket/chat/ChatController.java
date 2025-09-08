package com.example.class_step_websocket.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/save-form")
    public String saveChatForm() {
        return "save-form";
    }

    @PostMapping("/save")
    public String saveChat(@RequestParam("message") String message,
                           Model model) {
        chatService.saveChat(message);
        return "save-form";
    }

    @GetMapping("/")
    public String chatList(Model model) {
        model.addAttribute("models", chatService.chatList());
        return "index";
    }

    // MessageMapping -> 내부 동작 원리(AOP)
    // /app/chat -> 메시지를 방송처리
    // 순수 웹소켓 구현시 만들었던 웹 소켓 핸들러를 대체한다
    // MessageMapping -> handleMessage() 로직을 대체함
    // SendTo -> 기존의 로직인 broadcastMessage()를 대체
    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public ChatMessageDTO sendMessage(ChatMessageDTO chatMessageDTO) {
        try {
            log.debug("컨트롤러 진입 성공, 메시지: {}", chatMessageDTO.getContent());

            if (chatMessageDTO.getContent() == null || chatMessageDTO.getContent().trim().isEmpty()) {
                log.warn("빈 메시지가 수신되었습니다");
                return null;
            }

            Chat savedChat = chatService.saveChat(chatMessageDTO.getContent().trim());

            return ChatMessageDTO.builder()
                    .id(savedChat.getId().toString())
                    .content(savedChat.getMessage())
                    .sender(chatMessageDTO.getSender())
                    .type("CHAT")
                    .timestamp(LocalDateTime.now().toString())
                    .build();
        } catch (Exception e) {
            log.error("메시지 저장 오류, 원인: {}", e.getMessage());
            return createErrorMessage("메시지 전송 실패");
        }
    }

    // 에러 메시지 생성 메서드
    private ChatMessageDTO createErrorMessage(String errorMessage) {
        return ChatMessageDTO.builder()
                .id(System.currentTimeMillis() + "")
                .content(errorMessage)
                .type("SYSTEM")
                .timestamp(LocalDateTime.now().toString())
                .build();
    }
}
