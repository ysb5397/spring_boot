package com.puzzlix.solid_task.domain.user;

import com.puzzlix.solid_task.domain.user.dto.UserRequest;
import com.puzzlix.solid_task.domain.user.login.LocalLoginStrategy;
import com.puzzlix.solid_task.domain.user.login.LoginStrategy;
import com.puzzlix.solid_task.domain.user.login.LoginStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginStrategyFactory loginStrategyFactory;

    @Transactional
    public User signUp(UserRequest.SignUp request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new IllegalArgumentException("이미 사용중인 이메일입니다");

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(newUser);
    }

    public User login(String type, UserRequest.Login request) {
        LoginStrategy loginStrategy = loginStrategyFactory.findStrategy(type);
        return loginStrategy.login(request);
    }
}
