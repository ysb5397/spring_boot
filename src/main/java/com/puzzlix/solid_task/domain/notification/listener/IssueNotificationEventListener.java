package com.puzzlix.solid_task.domain.notification.listener;

import com.puzzlix.solid_task.domain.issue.Issue;
import com.puzzlix.solid_task.domain.issue.event.IssueStatusChangedEvent;
import com.puzzlix.solid_task.domain.notification.NotificationSender;
import com.puzzlix.solid_task.domain.notification.NotificationSenderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssueNotificationEventListener {

    private final NotificationSenderFactory senderFactory;

    @Value("${notification.policy.on-status-done}")
    private String onStatusDone;

    @EventListener
    public void handleIssueStatusChangeEvent(IssueStatusChangedEvent event) {
        Issue issue = event.getIssue();
        String message = "이슈 상태가 변경되었습니다.\nID: " + issue.getId() +
                            "\nStatus: " + issue.getStatus();

        if ("DONE".equalsIgnoreCase(issue.getStatus().name())) {
            NotificationSender sender = senderFactory.findSender(onStatusDone);
            sender.send(message);
        }
    }
}