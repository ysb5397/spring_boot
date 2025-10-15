package com.puzzlix.solid_task.domain.issue.event;

import com.puzzlix.solid_task.domain.issue.Issue;
import lombok.Data;

@Data
public class IssueStatusChangedEvent {

    private final Issue issue;

    public IssueStatusChangedEvent(Issue issue) {
        this.issue = issue;
    }
}
