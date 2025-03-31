package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusData;

    public JCFReadStatusRepository() {
        this.readStatusData = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        this.readStatusData.put(readStatus.getReadStatusId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusId) {
        return Optional.ofNullable(this.readStatusData.get(readStatusId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return this.readStatusData.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return this.readStatusData.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public boolean existsById(UUID readStatusId) {
        return this.readStatusData.containsKey(readStatusId);
    }

    @Override
    public void deleteById(UUID readStatusId) {
        this.readStatusData.remove(readStatusId);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId)
                .forEach(readStatus -> this.deleteById(readStatus.getReadStatusId()));
    }
}
