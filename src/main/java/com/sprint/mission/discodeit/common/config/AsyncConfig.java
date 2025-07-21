package com.sprint.mission.discodeit.common.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

  @Bean("uploadExecutor")
  public Executor S3Executor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(3);
    executor.setThreadNamePrefix("S3-Thread-");
    executor.setTaskDecorator(new ContextTaskDecorator());
    executor.initialize();

    return executor;
  }

  @Bean("notificationExecutor")
  public Executor NotificationExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(3);
    executor.setThreadNamePrefix("Notification-Thread-");
    executor.setTaskDecorator(new ContextTaskDecorator());
    executor.initialize();

    return executor;
  }

}
