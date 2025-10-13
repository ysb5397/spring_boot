package com.puzzlix.solid_task.domain.user.login;

import com.puzzlix.solid_task.domain.user.User;
import com.puzzlix.solid_task.domain.user.dto.UserRequest;

public interface LoginStrategy {
    User login(UserRequest.Login request);
    boolean supports(String type);
}
