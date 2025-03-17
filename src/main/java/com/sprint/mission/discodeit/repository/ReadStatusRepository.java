package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    Optional<ReadStatus> findByUserIdAndChannelId(Long userId, Long channelId);
    void save(ReadStatus readStatus);
    void delete(UUID id);
    List<UUID> findUserIdsByChannelId(UUID channelId);
}
