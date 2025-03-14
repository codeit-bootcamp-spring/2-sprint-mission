package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(UUID userUUID, UUID ChannelUUID);
    Optional<ReadStatus> find(UUID userUUID, UUID ChannelUUID);
    void update(UUID userUUID, UUID channelUUID);
}
