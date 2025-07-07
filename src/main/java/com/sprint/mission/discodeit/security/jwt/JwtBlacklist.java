package com.sprint.mission.discodeit.security.jwt;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 리프레쉬 토큰이 무효화됐음에도, 엑세스 토큰을 만료시간까지 사용할 수 있었던 것을 방지하기 위함
@Component
public class JwtBlacklist {

  private final ConcurrentHashMap<String, Date> blackList = new ConcurrentHashMap<>();

  public void addBlackList(String token, Date expiration) {
    blackList.put(token, expiration);
  }

  public boolean isBlacked(String token) {
    // 만료된 토큰이라면 블랙리스트에서도 제거 (메모리 누수 방지)
    Date expiration = blackList.get(token);
    if (expiration == null) {
      return false;
    }

    if (expiration.before(new Date())) {
      blackList.remove(token);
      return false;
    }

    return true;
  }

  // 60분 주기로 만료된 토큰 제거 (메모리 누수 방지)
  @Scheduled(fixedRate = 60_000)
  public void cleanup() {
    Date now = new Date();
    blackList.entrySet().removeIf(entry -> entry.getValue().before(now));
  }
}
