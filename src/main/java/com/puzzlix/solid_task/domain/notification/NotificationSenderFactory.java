package com.puzzlix.solid_task.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationSenderFactory {

    private final List<NotificationSender> notificationSenders;

    public NotificationSender findSender(String type) {
        for (NotificationSender sender : notificationSenders) {
            if (sender.supports(type)) return sender;
        }
        throw new IllegalArgumentException("지원하지 않는 알림 방식입니다. / [type]: " + type);
    }
}
