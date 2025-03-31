package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> findById(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    boolean existsById(UUID readStatusId);

    void deleteById(UUID readStatusId);

    void deleteAllByChannelId(UUID channelId);
}
