package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final BasicAuthService authService;

    @Override
    public void run(ApplicationArguments args) {
        authService.initAdmin();
    }
}
