package com.sprint.mission.discodeit.controller;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
// 캐시 히트, 미스 등을 보기 위한 모니터링 API
public class CacheMonitoringController {

  private final CacheManager cacheManager;

  public CacheMonitoringController(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @GetMapping("/stats")
  public Map<String, Object> getCacheStats() {
    Map<String, Object> stats = new HashMap<>();

    // 다른 캐시 인스턴스들은 지표를 제공하지 않거나 방식이 다름
    cacheManager.getCacheNames().forEach(cacheName -> {
      Cache cache = cacheManager.getCache(cacheName);
      if (cache instanceof CaffeineCache) {
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        var nativeCache = caffeineCache.getNativeCache();

        stats.put(cacheName, Map.of(
            "size", nativeCache.estimatedSize(),
            "requestCount", nativeCache.stats().requestCount(),
            "hitCount", nativeCache.stats().hitCount(),
            "missCount", nativeCache.stats().missCount(),
            "evictCount", nativeCache.stats().evictionCount(),
            "hitRate", String.format("%.2f%%", nativeCache.stats().hitRate() * 100)
            // 더 추가 가능
        ));
      }
    });
    return stats;
  }
}