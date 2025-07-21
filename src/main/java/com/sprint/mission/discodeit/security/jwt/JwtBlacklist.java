package com.sprint.mission.discodeit.security.jwt;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtBlacklist {

    private final Map<String, Instant> blacklist = new ConcurrentHashMap<>();

   
    public void blacklist(String accessToken, Instant expiryTime) {
        blacklist.put(accessToken, expiryTime);
    }

        
    public boolean isBlacklisted(String accessToken) {
        return blacklist.containsKey(accessToken);
    }

    // 30분으로 설정
    @Scheduled(cron = "${app.scheduling.blacklist-cleanup-cron}")
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        blacklist.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }
} 