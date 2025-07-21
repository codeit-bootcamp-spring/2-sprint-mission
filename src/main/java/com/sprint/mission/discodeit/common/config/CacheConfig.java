package com.sprint.mission.discodeit.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  public static final String CHANNEL_CACHE_NAME = "channels";
  public static final String NOTIFICATION_CACHE_NAME = "notifications";
  public static final String USER_CACHE_NAME = "users";

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(caffeineCacheBuilder());
    cacheManager.setCacheNames(
        List.of(CHANNEL_CACHE_NAME, NOTIFICATION_CACHE_NAME, USER_CACHE_NAME)
    );
    return cacheManager;
  }

  @Bean
  public Caffeine<Object, Object> caffeineCacheBuilder() {
    return Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(60, TimeUnit.MINUTES)
        .recordStats();
  }

}
