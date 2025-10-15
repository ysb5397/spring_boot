package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task.domain.issue.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class IssueChatController {


    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{issueId}")
    public void handleChatMessage(@DestinationVariable("issueId") Long issueId,
                                  ChatMessageDto messageDto) {

        messagingTemplate.convertAndSend("/topic/issues/" + issueId, messageDto);
    }
}
