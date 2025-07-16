package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {

    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    public void add(String accessToken, Instant expirationTime) {
        blacklist.put(accessToken, expirationTime);
    }

    public boolean contains(String accessToken) {
        Instant expirationTime = blacklist.get(accessToken);

        return expirationTime != null && expirationTime.isAfter(Instant.now());
    }

    public void removeExpiredTokens() {
        blacklist.entrySet().removeIf(
            entry -> entry.getValue().isBefore(Instant.now()));
    }

    @Scheduled(fixedDelay = 1_800_000) // 30분 간격
    public void scheduleCleanup() {
        removeExpiredTokens();
    }
}
