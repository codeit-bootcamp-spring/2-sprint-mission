package com.sprint.mission.discodeit.cache;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheMonitoringController {

  private final CacheManager cacheManager;

  @GetMapping("/stats")
  public Map<String, Object> getCacheStats() {
    Map<String, Object> stats = new HashMap<>();

    cacheManager.getCacheNames().forEach(cacheName -> {
      Cache cache = cacheManager.getCache(cacheName);
      if (cache instanceof CaffeineCache) {
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        var nativeCache = caffeineCache.getNativeCache();

        stats.put(cacheName, Map.of(
            "size", nativeCache.estimatedSize(),
            "hitCount", nativeCache.stats().hitCount(),
            "missCount", nativeCache.stats().missCount(),
            "hitRate", String.format("%.2f%%", nativeCache.stats().hitRate() * 100)
        ));
      }
    });
    return stats;
  }

  @GetMapping("/clear/{cacheName}")
  public String clearCache(@PathVariable String cacheName) {
    Cache cache = cacheManager.getCache(cacheName);
    if (cache != null) {
      cache.clear();
      return cacheName + " 캐시가 삭제되었습니다";
    }
    return "캐시를 찾을 수 없습니다: " + cacheName;
  }
}
