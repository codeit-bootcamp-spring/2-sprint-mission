package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.taskDecorator.MdcTaskDecorator;
import com.sprint.mission.discodeit.taskDecorator.SecurityContextTaskDecorator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableRetry
public class AsyncConfig {

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
}

