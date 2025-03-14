package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.Optional;

public interface ReadStatusRepository {
    Optional<ReadStatus> findByUserIdAndChannelId(Long userId, Long channelId);
    void save(ReadStatus readStatus);
    void delete(ReadStatus readStatus);
}
