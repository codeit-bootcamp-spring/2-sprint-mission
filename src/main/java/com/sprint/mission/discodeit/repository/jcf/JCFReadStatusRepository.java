package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        data = new HashMap<>();
    }

    @Override
    public void save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
    }

    @Override
    public Optional<ReadStatus> find(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(value -> value.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) { // channelId를 가지고 있는 ReadStatus들의 userId모음
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId)).toList();
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
