package com.sprint.mission.discodeit.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenStore {
    private final Map<UUID, String> tokenStore = new ConcurrentHashMap<>();

    public void save(UUID id, String token) {
        tokenStore.put(id, token);
    }

    public String getToken(UUID id) {
        System.out.println(tokenStore.keySet());
        return tokenStore.get(id);
    }

    public void delete(UUID id) {
        tokenStore.remove(id);
    }

}
