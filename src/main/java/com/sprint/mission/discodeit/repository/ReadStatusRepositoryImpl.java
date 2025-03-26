package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ReadStatusRepositoryImpl implements ReadStatusRepository {
    private final Map<String, ReadStatus> readStatusStore = new ConcurrentHashMap<>();

    private String generateKey(UUID userId, UUID channelId) {
        return userId.toString() + "_" + channelId.toString();
    }

    @Override
    public Optional<ReadStatus> findById(UUID userId, UUID channelId) {
        return Optional.ofNullable(readStatusStore.get(generateKey(userId, channelId)));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusStore.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusStore.put(generateKey(readStatus.getUserId(), readStatus.getChannelId()), readStatus);
        return readStatus;
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusStore.containsKey(generateKey(userId, channelId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusStore.values()
                .stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return readStatusStore.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        readStatusStore.remove(id);
    }

    @Override
    public void deleteByUserIdAndChannelId(UUID userId, UUID channelId) {
        readStatusStore.entrySet().removeIf(entry ->
                entry.getValue().getUserId().equals(userId) &&
                        entry.getValue().getChannelId().equals(channelId));
    }
}