package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .toList();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<UUID> findUserIdsByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .distinct()
                .toList();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .anyMatch(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        data.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
    }

    @Override
    public void deleteById(UUID id) {
        if (!data.containsKey(id)) {
            throw new NoSuchElementException("No such read status");
        }
        data.remove(id);
    }
}