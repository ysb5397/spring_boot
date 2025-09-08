package com.example.class_step_websocket.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private String id;
    private String content;
    private String sender;
    private String type;
    private String timestamp;

    // enum 타입으로 변환
    public MessageType getMessageType() {
        return "system".equals(this.type) ? MessageType.SYSTEM : MessageType.CHAT;
    }
}
