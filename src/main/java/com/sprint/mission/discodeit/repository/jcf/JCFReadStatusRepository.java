package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFReadStatusRepository implements ReadStatusRepository {
    Map<UUID, ReadStatus> data = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        return data.put(readStatus.getUuid(), readStatus);
    }

    @Override
    public void delete(UUID readStatusKey) {
        data.remove(readStatusKey);
    }

    @Override
    public ReadStatus findByKey(UUID readStatusKey) {
        return data.get(readStatusKey);
    }

    @Override
    public List<ReadStatus> findAllByUserKey(UUID userKey) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserKey().equals(userKey))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelKey(UUID channelKey) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelKey().equals(channelKey))
                .toList();
    }
}
