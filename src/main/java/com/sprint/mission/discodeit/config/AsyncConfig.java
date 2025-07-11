package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.MdcSecurityAwareExecutor;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

  @Bean(name = "storageTaskExecutor")
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new MdcSecurityAwareExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("storage-");
    executor.initialize();
    return executor;
  }
}

