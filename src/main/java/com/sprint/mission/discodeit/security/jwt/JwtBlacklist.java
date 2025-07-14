package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {

  private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

  public void add(String accessTocken, Instant expiration) {
    blacklist.put(accessTocken, expiration);
  }

  public boolean contains(String accessToken) {
    Instant expiration = blacklist.get(accessToken);
    if (expiration == null) {
      return false;
    }

    if (expiration.isBefore(Instant.now())) {
      blacklist.remove(accessToken);
      return false;
    }
    return true;
  }
}
