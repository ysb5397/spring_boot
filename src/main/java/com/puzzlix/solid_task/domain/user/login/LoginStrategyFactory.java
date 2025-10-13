package com.puzzlix.solid_task.domain.user.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginStrategyFactory {

    private final List<LoginStrategy> loginStrategies;

    public LoginStrategy findStrategy(String type) {
        for (LoginStrategy strategy : loginStrategies) {
            if (strategy.supports(type)) return strategy;
        }
        throw new IllegalArgumentException("해당하는 로그인 타입이 없습니다");
    }
}
