package com.puzzlix.solid_task.domain.notification;

import org.springframework.stereotype.Component;

@Component
public class SmsSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("=======================================\n" +
                "[Server] " + message +
                "\n=======================================");
    }

    @Override
    public boolean supports(String type) {
        return "SMS".equalsIgnoreCase(type);
    }
}
