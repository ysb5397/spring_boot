package com.puzzlix.solid_task.domain.issue;

import com.puzzlix.solid_task.domain.issue.dto.IssueRequest;
import com.puzzlix.solid_task.domain.project.Project;
import com.puzzlix.solid_task.domain.project.ProjectRepository;
import com.puzzlix.solid_task.domain.user.User;
import com.puzzlix.solid_task.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private IssueRepository issueRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private IssueService issueService;

    @Test
    void create_withMapping() {
        IssueRequest.Create create = new IssueRequest.Create();
        create.setTitle("제목");
        create.setDescription("내용");
        create.setProjectId(1L);
        create.setReporterId(1L);

        // ==============================
        User reporter = new User(1L, "테스트", "a@a.com", "1234", null);
        Project project = new Project(1L, "테스트입니다", "테스트 설명입니다", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(reporter));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(issueRepository.save(any(Issue.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Issue issue = issueService.create(create);

        Assertions.assertThat(issue.getDescription()).isEqualTo("내용");
        Assertions.assertThat(issue.getStatus()).isEqualTo(IssueStatus.TODO);

        // =======================
        Assertions.assertThat(issue.getReporter()).isEqualTo(reporter);
        Assertions.assertThat(issue.getProject()).isEqualTo(project);

        verify(userRepository).findById(1L);
        verify(projectRepository).findById(1L);
    }
}
