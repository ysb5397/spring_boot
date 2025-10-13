package com.puzzlix.solid_task.domain.user.login;

import com.puzzlix.solid_task.domain.user.User;
import com.puzzlix.solid_task.domain.user.UserRepository;
import com.puzzlix.solid_task.domain.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleLoginStrategy implements LoginStrategy {

    private final UserRepository userRepository;

    @Override
    public User login(UserRequest.Login request) {
        // TODO
        return null;
    }

    @Override
    public boolean supports(String type) {
        return "GOOGLE".equalsIgnoreCase(type);
    }
}
