package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("basicReadStatusRepository")
public class BasicReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> store = new HashMap<>();

    @Override
    public boolean register(ReadStatus readStatus) {
        store.put(readStatus.getId(), readStatus);
        return true;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return store.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return store.values().stream()
                .filter(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public boolean updateReadStatus(ReadStatus readStatus) {
        if (!store.containsKey(readStatus.getId())) {
            return false;
        }
        store.put(readStatus.getId(), readStatus);
        return true;
    }

    @Override
    public boolean deleteReadStatus(UUID id) {
        return store.remove(id) != null;
    }

    @Override
    public boolean deleteAllByChannelId(UUID channelId) {
        List<UUID> keysToRemove = store.values().stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .map(ReadStatus::getId)
                .collect(Collectors.toList());

        keysToRemove.forEach(store::remove);
        return true;
    }

    @Override
    public boolean deleteAllByUserId(UUID userId) {
        List<UUID> keysToRemove = store.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .map(ReadStatus::getId)
                .collect(Collectors.toList());

        keysToRemove.forEach(store::remove);
        return true;
    }
} 