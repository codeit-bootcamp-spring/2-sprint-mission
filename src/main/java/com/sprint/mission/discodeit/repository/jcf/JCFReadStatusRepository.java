package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.Empty.EmptyReadStatusListException;
import com.sprint.mission.discodeit.exception.NotFound.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusList = new ConcurrentHashMap<>();

//    private final List<ReadStatus> readStatusList = new ArrayList<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusList.put(readStatus.getReadStatusId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusId) {
        return Optional.ofNullable(this.readStatusList.get(readStatusId));
    }

    @Override
    public ReadStatus findByUserId(UUID userId) {
        return readStatusList.values().stream().filter(readStatus -> readStatus.getUserId().equals(userId)).findFirst().orElse(null);
    }

    @Override
    public ReadStatus findByChannelId(UUID channelId) {
        return readStatusList.values().stream().filter(readStatus -> readStatus.getChannelId().equals(channelId)).findFirst().orElse(null);
    }

    @Override
    public ReadStatus findByUserAndChannelId(UUID userId, UUID channelId) {
        return readStatusList.values().stream().filter(readStatus ->readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId)).findFirst().orElse(null);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        List<ReadStatus> list = this.readStatusList.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userID))
                .toList();
        return list;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        List<ReadStatus> list = this.readStatusList.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
        return list;
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusList.remove(readStatusId);
    }
}
