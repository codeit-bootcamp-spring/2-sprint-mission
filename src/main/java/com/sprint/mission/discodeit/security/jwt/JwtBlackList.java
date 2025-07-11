package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtBlackList {

    Map<String, Instant> blackList = new ConcurrentHashMap<>();

    public void put(String accessToken, Instant expirationAt) {
        blackList.put(accessToken, expirationAt);
    }

    public boolean check(String accessToken) {
        return blackList.containsKey(accessToken);
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void cleanExpired() {
        blackList.values().removeIf(expirationAt -> Instant.now().isAfter(expirationAt));
    }

}
