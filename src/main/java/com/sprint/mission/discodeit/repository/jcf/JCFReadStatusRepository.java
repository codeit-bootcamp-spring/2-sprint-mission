package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<String, ReadStatus> readStatusMap = new HashMap<>();

    private String generateKey(UUID userId, UUID channelId) {
        return userId.toString() + "-" + channelId.toString();
    }

    @Override
    public void save(ReadStatus readStatus) {
        String key = generateKey(readStatus.getUser().getId(), readStatus.getChannel().getId());
        readStatusMap.put(key, readStatus);
    }

    @Override
    public Optional<ReadStatus> findById(UUID userId, UUID channelId) {
        String key = generateKey(userId, channelId);
        return Optional.ofNullable(readStatusMap.get(key));
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatusMap.values());
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID userId, UUID channelId) {
        String key = generateKey(userId, channelId);
        readStatusMap.remove(key);
    }
}
