package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final HashMap<UUID, ReadStatus> readStatuss = new HashMap<>();

    @Override
    public void save(ReadStatus readStatus) {
        readStatuss.put(readStatus.getId(), readStatus);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatuss.get(id));
    }

    @Override
    public Optional<List<ReadStatus>> findAll() {
        return Optional.of(new ArrayList<>(readStatuss.values()));
    }

    @Override
    public Optional<List<ReadStatus>> findAllByUserId(UUID userId) {
        return Optional.of(new ArrayList<>(readStatuss.values().stream().filter(readStatus -> readStatus.getUserId().equals(userId)).collect(Collectors.toList())));
    }

    @Override
    public void update(ReadStatus readStatus) {
        save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        readStatuss.remove(id);
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatuss.values().stream().anyMatch(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId));
    }
}
