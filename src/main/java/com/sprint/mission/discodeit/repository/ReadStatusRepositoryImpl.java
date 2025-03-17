package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class ReadStatusRepositoryImpl implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusStore = new HashMap<>();

    @Override
    public ReadStatus findById(UUID userId, UUID channelId) {
        return readStatusStore.values().stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId))
            .findFirst()
            .orElse(null);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusStore.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
    }

    public void save(ReadStatus readStatus) {
        readStatusStore.put(UUID.randomUUID(), readStatus);
    }
}