package com.sprint.mission.discodeit.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

  private static final int CORE_POOL_SIZE = 5;
  private static final int MAXIMUM_POOL_SIZE = 10;
  private static final int QUEUE_CAPACITY = 25;

  @Bean(name = "taskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(CORE_POOL_SIZE);
    executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
    executor.setQueueCapacity(QUEUE_CAPACITY);
    executor.setThreadNamePrefix("Async-");
    // Task Decorator 적용 (MDC 전파)
    executor.setTaskDecorator(new ContextTaskDecorator());
    executor.initialize();

    // SecurityContext 전파를 위한 래핑
    return new DelegatingSecurityContextExecutor(executor);
  }
}
