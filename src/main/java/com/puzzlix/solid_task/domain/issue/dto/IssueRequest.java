package com.puzzlix.solid_task.domain.issue.dto;

import com.puzzlix.solid_task.domain.project.Project;
import com.puzzlix.solid_task.domain.user.User;
import lombok.Data;

public class IssueRequest {

    @Data
    public static class Create {
        private String title;
        private String description;
        private Long projectId;
        private Long reporterId;
    }
}
