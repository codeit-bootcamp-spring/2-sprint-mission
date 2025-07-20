package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public Caffeine<Object, Object> caffeineConfig() {
    return Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(1000);
  }

  @Bean
  public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager(
        "channelsByUser", "notificationsByUser", "allUsers"
    );
    cacheManager.setCaffeine(caffeine);
    return cacheManager;
  }
}
