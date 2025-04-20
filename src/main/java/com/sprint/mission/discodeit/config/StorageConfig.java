package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.config.condition.LocalStorageCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageConfig {

  @Value("${discodeit.storage.local.root-path}")
  private String rootPath;

  @Bean
  @Conditional(LocalStorageCondition.class) // 조건이 맞을 때만 등록
  public Path localStorageRootPath() {
    return Paths.get(rootPath);
  }
}