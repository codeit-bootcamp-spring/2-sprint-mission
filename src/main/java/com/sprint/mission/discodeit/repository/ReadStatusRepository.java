package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    Optional<List<ReadStatus>> findAll();
    Optional<List<ReadStatus>> findAllByUserId(UUID userId);
    void update(ReadStatus readStatus);
    void delete(UUID id);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
}

