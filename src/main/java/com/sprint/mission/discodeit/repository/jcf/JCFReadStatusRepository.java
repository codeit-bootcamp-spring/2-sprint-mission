package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {
    private static final Map<UUID, ReadStatus> readStatusMap = new HashMap<>();

    @Override
    public void save() {
    }

    @Override
    public void addReadStatus(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .findFirst()
                .ifPresent(readStatus -> readStatus.addUser(userId));
    }

    @Override
    public ReadStatus findReadStatusById(UUID id) {
        return readStatusMap.get(id);
    }

    @Override
    public List<ReadStatus> findAllReadStatus() {
        return new ArrayList<>(readStatusMap.values());
    }

    @Override
    public void updateTime(UUID channelId, UUID userId) {
        readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .findFirst()
                .ifPresent(readStatus -> readStatus.updateLastAccessTime(userId));
    }

    @Override
    public void deleteReadStatusById(UUID id) {
        readStatusMap.remove(id);
    }

    @Override
    public boolean existReadStatusById(UUID id) {
        return readStatusMap.containsKey(id);
    }
}
