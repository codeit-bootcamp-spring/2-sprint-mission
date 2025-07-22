package com.sprint.mission.discodeit.event;

import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelCacheEvictionListener {

    private final CacheManager cacheManager;

    @EventListener
    public void handlePrivateChannelCreated(PrivateChannelCreatedEvent event) {
        Cache userChannelsCache = Objects.requireNonNull(cacheManager.getCache("userChannels"));
        for (UUID userId : event.participantIds()) {
            userChannelsCache.evict(userId);
        }

        Cache allUsersCache = Objects.requireNonNull(cacheManager.getCache("findAllUsers"));
        allUsersCache.clear();
    }
}