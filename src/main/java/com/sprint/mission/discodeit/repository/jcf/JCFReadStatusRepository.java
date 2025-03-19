package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final List<ReadStatus> readStatusList = new ArrayList<>();

    @Override
    public void save(ReadStatus readStatus) {
        readStatusList.add(readStatus);
    }

    @Override
    public Optional<ReadStatus> find(UUID readStatusUUID) {
        return readStatusList.stream()
                .filter(readStatus -> readStatus.getId().equals(readStatusUUID))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userUUID) {
        return readStatusList.stream()
                .filter(readStatus -> readStatus.getUserId().equals(userUUID))
                .toList();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelUUID) {
        return readStatusList.stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelUUID))
                .toList();
    }

    @Override
    public void update(UUID userUUID, UUID channelUUID) {
        findByUserId(userUUID).stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelUUID))
                .findAny()
                .ifPresent(ReadStatus::updateTime);
    }

    @Override
    public void delete(UUID readStatusUUID) {
        readStatusList.removeIf(readStatus -> readStatus.getId().equals(readStatusUUID));
    }
}
