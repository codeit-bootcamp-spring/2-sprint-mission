package com.sprint.mission.discodeit.common.config.async;

import java.util.List;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.support.CompositeTaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

  @Bean("uploadExecutor")
  public Executor s3Executor(TaskDecorator taskDecorator) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(3);
    executor.setThreadNamePrefix("S3-Thread-");
    executor.setTaskDecorator(taskDecorator);
    executor.initialize();

    return executor;
  }

  @Bean("notificationExecutor")
  public Executor notificationExecutor(TaskDecorator taskDecorator) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(3);
    executor.setThreadNamePrefix("Notification-Thread-");
    executor.setTaskDecorator(taskDecorator);
    executor.initialize();

    return executor;
  }

  @Bean("kafkaExecutor")
  public Executor kafkaExecutor(TaskDecorator taskDecorator) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(3);
    executor.setMaxPoolSize(5);
    executor.setQueueCapacity(50);
    executor.setThreadNamePrefix("kafka-relay-");
    executor.setTaskDecorator(taskDecorator);
    executor.initialize();

    return executor;
  }

  @Bean
  public TaskDecorator taskDecorator() {
    List<TaskDecorator> decorators = List.of(
        new MDCContextTaskDecorator(),
        new SecurityContextTaskDecorator()
    );
    return new CompositeTaskDecorator(decorators);
  }

}
