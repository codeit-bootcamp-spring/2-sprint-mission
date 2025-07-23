package com.sprint.mission.discodeit.common.config;

import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {

  public static final String CHANNEL_CACHE_NAME = "channels";
  public static final String NOTIFICATION_CACHE_NAME = "notifications";
  public static final String USER_CACHE_NAME = "users";

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCacheNames(
        List.of(CHANNEL_CACHE_NAME, NOTIFICATION_CACHE_NAME, USER_CACHE_NAME)
    );
    return cacheManager;
  }

}
