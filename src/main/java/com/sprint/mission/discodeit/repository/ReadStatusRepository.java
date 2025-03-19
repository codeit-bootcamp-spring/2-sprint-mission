package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.SaveReadStatusParamDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    Optional<ReadStatus> find(UUID readStatusUUID);
    List<ReadStatus> findByUserId(UUID userUUID);
    List<ReadStatus> findByChannelId(UUID channelUUID);
    void update(UUID userUUID, UUID ChannelUUID);
    void delete(UUID readStatusUUID);
}
