package com.sprint.mission.discodeit.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartupConfigLogger {

    private final Environment environment;

    public StartupConfigLogger(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void showLog() {
        log.info("==================== Key Configurations ====================");
        log.info("Active Spring Profiles: {}", String.join(", ", environment.getActiveProfiles()));
        log.info("SPRING_DATASOURCE_URL: {}", environment.getProperty("spring.datasource.url"));
        log.info("SPRING_DATASOURCE_USERNAME: {}", environment.getProperty("spring.datasource.username"));
        log.info("============================================================");
    }
}
