package com.sprint.mission.discodeit.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
@EnableRetry
public class AsyncConfig {

  @Bean
  public TaskExecutor binaryContentTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("binary-content-");
    executor.setCorePoolSize(3);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(20);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setKeepAliveSeconds((int) Duration.ofSeconds(30).toSeconds());

    executor.setTaskDecorator(new MDCTaskDecorator());
    executor.initialize();

    return new DelegatingSecurityContextAsyncTaskExecutor(executor);
  }

  @Bean
  public TaskExecutor asyncTaskFailureExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("failure-log-");
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(5);
    executor.setQueueCapacity(10);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setKeepAliveSeconds((int) Duration.ofSeconds(30).toSeconds());

    executor.setTaskDecorator(new MDCTaskDecorator());
    executor.initialize();

    return new DelegatingSecurityContextAsyncTaskExecutor(executor);
  }

  @Bean
  public TaskExecutor notificationTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("notification-");
    executor.setCorePoolSize(3);
    executor.setMaxPoolSize(8);
    executor.setQueueCapacity(50);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.setKeepAliveSeconds((int) Duration.ofSeconds(30).toSeconds());

    executor.setTaskDecorator(new MDCTaskDecorator());
    executor.initialize();

    return new DelegatingSecurityContextAsyncTaskExecutor(executor);
  }

  public static class MDCTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
      Map<String, String> contextMap = MDC.getCopyOfContextMap();
      return () -> {
        if (contextMap != null) {
          MDC.setContextMap(contextMap);
        }
        try {
          runnable.run();
        } finally {
          MDC.clear();
        }
      };
    }

  }

}
