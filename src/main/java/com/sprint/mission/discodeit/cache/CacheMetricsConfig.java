package com.sprint.mission.discodeit.cache;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class CacheMetricsConfig {

  private final CacheManager cacheManager;
  private final MeterRegistry meterRegistry;
  private final CacheProperties cacheProperties;

  @PostConstruct
  public void bindCacheMetrics() {
    List<String> cacheNames = cacheProperties.getCacheNames();

    if (!cacheNames.isEmpty()) {
      cacheNames.forEach(cacheName -> {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
        if (cache != null) {
          CaffeineCacheMetrics.monitor(meterRegistry, cache.getNativeCache(), cacheName);
        }
      });
    }
  }
}