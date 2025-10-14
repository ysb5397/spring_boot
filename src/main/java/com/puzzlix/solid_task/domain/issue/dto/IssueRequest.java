package com.puzzlix.solid_task.domain.issue.dto;
import lombok.Data;

public class IssueRequest {

    @Data
    public static class Create {
        private String title;
        private String description;
        private Long projectId;
        private Long reporterId;
    }

    @Data
    public static class Update {
        private String title;
        private String description;
        private Long assigneeId;
    }
}
