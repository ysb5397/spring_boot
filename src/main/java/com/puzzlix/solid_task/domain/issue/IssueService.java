package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task.domain.issue.dto.IssueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    public Issue create(IssueRequest.Create createIssue) {
        Issue issue = new Issue();
        issue.setTitle(createIssue.getTitle());
        issue.setDescription(createIssue.getDescription());
        issue.setReporterId(createIssue.getReporterId());
        issue.setProjectId(createIssue.getProjectId());
        issue.setStatus(IssueStatus.TODO);
        return issueRepository.save(issue);
    }

    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    public Issue find(Long id) {
        return issueRepository.findById(id).orElse(null);
    }
}
