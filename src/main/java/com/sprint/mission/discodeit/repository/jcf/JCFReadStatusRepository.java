package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatuses = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatuses.put(readStatus.getId(), readStatus);

        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findByReadStatusId(UUID readStatusId) {
        return Optional.ofNullable(readStatuses.get(readStatusId));
    }

    @Override
    public Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId) {
        return readStatuses.values()
                .stream()
                .filter(readStatus -> readStatus.getChannel().getId().equals(channelId) && readStatus.getUser().getId().equals(userId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        return readStatuses.values()
                .stream()
                .filter(readStatus -> readStatus.getChannel().getId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        return readStatuses.values()
                .stream()
                .filter(readStatus -> readStatus.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatuses.remove(readStatusId);
    }
}
