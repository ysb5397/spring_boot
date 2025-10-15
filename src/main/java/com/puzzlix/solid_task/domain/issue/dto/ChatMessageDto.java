package com.puzzlix.solid_task.domain.issue.dto;

import lombok.Data;

@Data
public class ChatMessageDto {

    private Long issueId;
    private String sender;
    private String content;
}
