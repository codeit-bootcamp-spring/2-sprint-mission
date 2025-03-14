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

    private final List<ReadStatus> ReadStatusList = new ArrayList<>();

    @Override
    public ReadStatus save(UUID userUUID, UUID ChannelId) {
        ReadStatus readStatus = new ReadStatus(userUUID, ChannelId);
        ReadStatusList.add(readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> find(UUID userUUID, UUID channelUUID) {
        return ReadStatusList.stream()
                .filter(readStatus -> readStatus.getUserId().equals(userUUID) && readStatus.getChannelId().equals(channelUUID))
                .findAny();
    }

    @Override
    public void update(UUID userUUID, UUID channelUUID) {
        find(userUUID, channelUUID).ifPresent(ReadStatus::updateTime);
    }
}
