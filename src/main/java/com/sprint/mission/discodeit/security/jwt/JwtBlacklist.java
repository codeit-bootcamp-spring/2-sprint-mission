package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {

  private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

  public void add(String token, Instant expiry) {
    blacklist.put(token, expiry);
  }

  public boolean isBlacklisted(String token) {
    return blacklist.containsKey(token);
  }

  @Scheduled(fixedRate = 600_000)
  public void cleanup() {
    Instant now = Instant.now();
    blacklist.entrySet().removeIf(entry -> now.isAfter(entry.getValue()));
  }
}
