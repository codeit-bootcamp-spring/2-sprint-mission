package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.event.AsyncFailedEvent;
import com.sprint.mission.discodeit.taskDecorator.MdcTaskDecorator;
import com.sprint.mission.discodeit.taskDecorator.SecurityContextTaskDecorator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableRetry
@RequiredArgsConstructor
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    private final ApplicationEventPublisher publisher;

    @Bean("loggingTaskExecutor")
    public ThreadPoolTaskExecutor loggingTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("logging-async-");

        // MdcTaskDecorator 연결
        executor.setTaskDecorator(new MdcTaskDecorator());

        executor.initialize();
        return executor;
    }

    @Bean("securityTaskExecutor")
    public ThreadPoolTaskExecutor securityTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(6);
        executor.setThreadNamePrefix("security-async-");

        // SecurityContextTaskDecorator 연결
        executor.setTaskDecorator(new SecurityContextTaskDecorator());

        executor.initialize();
        return executor;
    }

    @Bean("combinedExecutor")
    public Executor combinedExecutor(@Qualifier("loggingTaskExecutor") ThreadPoolTaskExecutor loggingTaskExecutor) {
        return new DelegatingSecurityContextAsyncTaskExecutor(loggingTaskExecutor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                publisher.publishEvent(new AsyncFailedEvent(method.getName(), ex));
            }
        };
    }
}

