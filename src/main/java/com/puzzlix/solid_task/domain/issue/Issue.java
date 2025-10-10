package com.puzzlix.solid_task.domain.issue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Issue {

    private Long id;
    private String title;
    private String description;
    private IssueStatus status;

    private Long projectId;
    private Long reporterId;
    private Long assigneeId;
}
