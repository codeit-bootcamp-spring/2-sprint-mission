package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.service.AuthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {
    private final AuthService authService;

    @PostConstruct
    public void init() {
        authService.initAdmin();
    }
}
