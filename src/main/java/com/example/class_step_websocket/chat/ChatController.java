package com.example.class_step_websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
