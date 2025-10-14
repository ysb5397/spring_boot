package com.puzzlix.solid_task._global.config;

import com.puzzlix.solid_task.domain.issue.Issue;
import com.puzzlix.solid_task.domain.issue.IssueRepository;
import com.puzzlix.solid_task.domain.issue.IssueStatus;
import com.puzzlix.solid_task.domain.project.Project;
import com.puzzlix.solid_task.domain.project.ProjectRepository;
import com.puzzlix.solid_task.domain.user.Role;
import com.puzzlix.solid_task.domain.user.User;
import com.puzzlix.solid_task.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final IssueRepository issueRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User testUser1 = userRepository.save(new User(null, "홍길동", "a@a.com",
                passwordEncoder.encode("1234"), Role.USER, null));
        User testUser2 = userRepository.save(new User(null, "ㄴㅁㅇㅁㄴㅇ", "b@a.com",
                passwordEncoder.encode("1234123"), Role.USER, null));
        User testAdmin1 = userRepository.save(new User(null, "admin", "admin@admin.com",
                passwordEncoder.encode("admin123"), Role.ADMIN, null));

        Project project1 = projectRepository.save(new Project(null, "test", "test입니다", null));
        Project project2 = projectRepository.save(new Project(null, "test2", "test2입니다", null));

        Issue issue1 = issueRepository.save(new Issue(null, "로그인 기능 구현", "aaa", IssueStatus.TODO, project2, testUser1, testUser2, null));
        Issue issue2 = issueRepository.save(new Issue(null, "회원가입 기능 구현", "bbbb", IssueStatus.TODO, project1, testUser2, testUser2, null));
    }
}
