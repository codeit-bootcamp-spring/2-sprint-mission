package com.sprint.mission.discodeit.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class JwtBlacklist {

    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

    public void blacklist(String accessToken, Instant expiryTime) {
        if (accessToken != null && expiryTime != null) {
            blacklist.put(accessToken, expiryTime);
        }
    }


    public boolean isBlacklisted(String accessToken) {
        return accessToken != null && blacklist.containsKey(accessToken);
    }


    @Scheduled(cron = "${app.scheduling.blacklist-cleanup-cron}")
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();

        blacklist.entrySet().removeIf(entry -> entry.getValue().isBefore(now));

    }

}