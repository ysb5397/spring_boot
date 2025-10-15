package com.puzzlix.solid_task.domain.notification;

import org.springframework.stereotype.Component;

@Component
public class EmailSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("=======================================\n" +
                "[이메일 발송] " + message +
                "\n=======================================");
    }

    @Override
    public boolean supports(String type) {
        return "EMAIL".equalsIgnoreCase(type);
    }
}
