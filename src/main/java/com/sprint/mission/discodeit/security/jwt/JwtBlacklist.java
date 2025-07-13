package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtBlacklist {

  private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

  public void add(String accessToken, Instant expirationTime) {
    blacklist.put(accessToken, expirationTime);
  }

  public boolean isBlacklisted(String accessToken) {
    return blacklist.containsKey(accessToken);
  }

  @Scheduled(fixedDelay = 60 * 60 * 1000)
  public void cleanUpExpiredTokens() {
    if (blacklist.isEmpty()) {
      return;
    }
    Instant now = Instant.now();
    blacklist.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    log.debug("Expired JWT 블랙리스트 정리 완료");
  }

}
