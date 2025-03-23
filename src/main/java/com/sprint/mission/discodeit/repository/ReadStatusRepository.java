package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> find(UUID readStatusId);

    List<ReadStatus> findByChannelId(UUID channelId);

    List<ReadStatus> findByUserId(UUID userId);

    ReadStatus updateLastReadTime(UUID readStatusId);

    void delete(UUID readStatusId);

    void deleteByChannelId(UUID channelId);
}
