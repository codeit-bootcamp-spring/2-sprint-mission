package com.sprint.mission.discodeit.config;

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

  @Bean
  public Executor executor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //i5-14400F
    //6P + 4E, 16Threads
    //CPU 집약적 작업: 코어 수 + 1
    //I/O 집약적 작업: 코어 수 × (1 + 대기시간/처리시간)
    //멀티 스레드 작업을 위해 4개 스레드만 할당
    //P코어 할당될 경우, 3스레드로 줄일 예정
    executor.setCorePoolSize(4); // 기본 스레드 수
    executor.setMaxPoolSize(8); //최대 스레드 수
//    executor.setThreadNamePrefix("Async-");
    executor.initialize();
    return executor;
  }
}
