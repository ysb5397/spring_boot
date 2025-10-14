package com.puzzlix.solid_task.domain.issue.dto;

import com.puzzlix.solid_task.domain.issue.Issue;
import com.puzzlix.solid_task.domain.issue.IssueStatus;
import lombok.Data;

import java.util.List;

public class IssueResponse {

    @Data
    public static class FindAll {
        private final Long id;
        private final String title;
        private final IssueStatus status;
        private final String reporterName;

        private FindAll(Issue issue) {
            this.id = issue.getId();
            this.title = issue.getTitle();
            this.status = issue.getStatus();
            this.reporterName = issue.getReporter().getName();
        }

        public static List<FindAll> from(List<Issue> issues) {
            return issues.stream().map(FindAll::new).toList();
        }
    }

    @Data
    public static class FindById {
        private final Long id;
        private final String title;
        private final String description;
        private final IssueStatus status;
        private final String projectName;
        private final String reporterName;
        private final String assigneeName;

        public FindById(Issue issue) {
            this.id = issue.getId();
            this.title = issue.getTitle();
            this.description = issue.getDescription();
            this.status = issue.getStatus();
            this.projectName = issue.getProject().getName();
            this.reporterName = issue.getReporter().getName();
            this.assigneeName = issue.getAssignee() != null ? issue.getAssignee().getName() : null;
        }
    }
}
