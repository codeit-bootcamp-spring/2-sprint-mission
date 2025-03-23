package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusMap;

    public JCFReadStatusRepository() {
        this.readStatusMap = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        this.readStatusMap.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusMap.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return readStatusMap.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        readStatusMap.remove(id);
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> result = new ArrayList<>();
        for (ReadStatus readStatus : readStatusMap.values()) {
            if (readStatus.getUserId().equals(userId)) {
                result.add(readStatus);
            }
        }
        return result;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        List<ReadStatus> result = new ArrayList<>();
        for (ReadStatus readStatus : readStatusMap.values()) {
            if (readStatus.getChannelId().equals(channelId)) {
                result.add(readStatus);
            }
        }
        return result;
    }
}
