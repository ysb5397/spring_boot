package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task.domain.issue.dto.IssueRequest;
import com.puzzlix.solid_task.domain.issue.dto.IssueResponse;
import com.puzzlix.solid_task.domain.issue.event.IssueStatusChangedEvent;
import com.puzzlix.solid_task.domain.project.Project;
import com.puzzlix.solid_task.domain.project.ProjectRepository;
import com.puzzlix.solid_task.domain.user.Role;
import com.puzzlix.solid_task.domain.user.User;
import com.puzzlix.solid_task.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public IssueResponse.FindById create(IssueRequest.Create createIssue) {
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
        return new IssueResponse.FindById(issueRepository.save(issue));
    }

    public List<IssueResponse.FindAll> findAll() {
        return IssueResponse.FindAll.from(issueRepository.findAll());
    }

    public IssueResponse.FindById find(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException("해당 이슈가 존재하지 않습니다."));
        return new IssueResponse.FindById(issue);
    }

    @Transactional
    public IssueResponse.FindById update(Long issueId, IssueRequest.Update request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다"));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 이슈를 찾을 수 없습니다"));

        if(!user.getRole().equals(Role.ADMIN) && !issue.getReporter().equals(user))
            throw new SecurityException("관리자 또는 보고자만 수정 가능합니다.");

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new NoSuchElementException("해당 ID의 담당자를 찾을 수 없습니다"));
            issue.setAssignee(assignee);
        } else {
            issue.setAssignee(null);
        }

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());

        return new IssueResponse.FindById(issue);
    }

    @Transactional
    public IssueResponse.FindById updateStatus(Long issueId, IssueStatus status, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다"));

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 이슈를 찾을 수 없습니다"));

        if(!user.getRole().equals(Role.ADMIN) && !issue.getAssignee().equals(user))
            throw new SecurityException("관리자 또는 담당자만 수정 가능합니다.");

        issue.setStatus(status);

        if (status == IssueStatus.DONE)
            eventPublisher.publishEvent(new IssueStatusChangedEvent(issue));

        return new IssueResponse.FindById(issue);
    }

    @Transactional
    public void delete(Long issueId, String userEmail) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 이슈를 찾을 수 없습니다"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다"));
        
        if(!user.getRole().equals(Role.ADMIN) && !issue.getReporter().getEmail().equals(userEmail))
            throw new IllegalArgumentException("관리자 또는 보고자만 삭제 가능합니다.");

        issueRepository.delete(issue);
    }
}
