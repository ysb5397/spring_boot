package com.example.class_step_websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional
    public Chat saveChat(String message) {
        Chat chat = Chat.builder().message(message).build();
        Chat savedChat = chatRepository.save(chat);
        return savedChat;
    }

    public List<Chat> chatList() {
        Sort desc = Sort.by(Sort.Direction.ASC, "id");
        return chatRepository.findAll(desc);
    }
}
