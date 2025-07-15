package com.sprint.mission.discodeit.config.async;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableRetry
public class AsyncConfig {
    @Bean(name="contextAwareTaskExecutor")
    public Executor contextAwareTaskExecutor(
            ContextCopyingTaskDecorator contextCopyingTaskDecorator) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncUploader-");
        executor.setTaskDecorator(contextCopyingTaskDecorator);// TaskDecorator 설정
        executor.initialize();
        return executor;
    }
}
