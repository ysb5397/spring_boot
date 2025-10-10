package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task.domain.issue.dto.IssueRequest;
import com.puzzlix.solid_task.domain.project.Project;
import com.puzzlix.solid_task.domain.project.ProjectRepository;
import com.puzzlix.solid_task.domain.user.User;
import com.puzzlix.solid_task.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Issue create(IssueRequest.Create createIssue) {
        User reporter = userRepository.findById(createIssue.getReporterId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저"));

        Project project = projectRepository.findById(createIssue.getProjectId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 프로젝트"));

        Issue issue = new Issue();
        issue.setTitle(createIssue.getTitle());
        issue.setDescription(createIssue.getDescription());
        issue.setReporter(reporter);
        issue.setProject(project);
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
