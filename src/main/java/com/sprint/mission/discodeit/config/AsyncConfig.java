package com.sprint.mission.discodeit.config;

import java.util.Map;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Configuration
public class AsyncConfig {

  public static class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
      Map<String, String> mdcContext = MDC.getCopyOfContextMap();
      SecurityContext securityContext = SecurityContextHolder.getContext();
      return () -> {
        try {
          if (mdcContext != null) MDC.setContextMap(mdcContext);
          SecurityContextHolder.setContext(securityContext);
          runnable.run();
        } finally {
          MDC.clear();
          SecurityContextHolder.clearContext();
        }
      };
    }
  }

  private static ThreadPoolTaskExecutor createExecutor(String namePrefix, int corePoolSize, int maxPoolSize) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setQueueCapacity(50);
    executor.setThreadNamePrefix(namePrefix);
    executor.setTaskDecorator(new MdcTaskDecorator());
    executor.initialize();
    return executor;
  }

  @Bean(name = "fileUploadTaskExecutor")
  public Executor fileUploadTaskExecutor() {
    return createExecutor("file-upload-", 5, 10);
  }

  @Bean(name = "notificationTaskExecutor")
  public Executor notificationTaskExecutor() {
    return createExecutor("notification-", 2, 5);
  }
}