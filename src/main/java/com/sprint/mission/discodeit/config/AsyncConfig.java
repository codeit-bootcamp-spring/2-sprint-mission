package com.sprint.mission.discodeit.config;

import java.util.List;
import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.CompositeTaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "binaryContentTaskExecutor")
    public TaskExecutor binaryContentTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("BinaryContent-");
        executor.setTaskDecorator(new CompositeTaskDecorator(
            List.of(mdcTaskDecorator(), securityContextTaskDecorator())));
        executor.initialize();
        return executor;
    }

    public TaskDecorator mdcTaskDecorator() {
        return runnable -> {
            // 현재 스레드 MDC에서 requestId 꺼냄
            Optional<String> requestId = Optional.ofNullable(
                MDC.get(MDCLoggingInterceptor.REQUEST_ID));
            return () -> {
                // 비동기 스레드에 requestId 설정
                requestId.ifPresent(id -> MDC.put(MDCLoggingInterceptor.REQUEST_ID, id));
                try {
                    // 비동기 작업 실행
                    runnable.run();
                } finally {
                    // 작업 끝나면 clean-up
                    requestId.ifPresent(id -> MDC.remove(MDCLoggingInterceptor.REQUEST_ID));
                }
            };
        };
    }

    public TaskDecorator securityContextTaskDecorator() {
        return runnable -> {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            return () -> {
                SecurityContextHolder.setContext(securityContext);
                try {
                    runnable.run();
                } finally {
                    SecurityContextHolder.clearContext();
                }
            };
        };
    }

}
