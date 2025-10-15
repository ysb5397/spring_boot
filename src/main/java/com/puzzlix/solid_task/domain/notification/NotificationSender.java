package com.puzzlix.solid_task.domain.notification;

public interface NotificationSender {

    void send(String message);
    boolean supports(String type);
}
