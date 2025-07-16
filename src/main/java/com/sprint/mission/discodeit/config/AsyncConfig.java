package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.async.ContextTaskDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public TaskDecorator contextTaskDecorator() {
        return new ContextTaskDecorator();
    }

    @Bean(name = "fileUploadExecutor")
    public ThreadPoolTaskExecutor fileUploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("fileUploadExecutor-");

        executor.setTaskDecorator(contextTaskDecorator());
        executor.initialize();
        return executor;
    }

}
