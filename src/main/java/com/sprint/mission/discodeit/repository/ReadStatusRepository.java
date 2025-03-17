package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    ReadStatus save(ReadStatus readStatus);
    void delete(UUID id);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<UUID> findUserIdsByChannelId(UUID channelId);
    Boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
}
