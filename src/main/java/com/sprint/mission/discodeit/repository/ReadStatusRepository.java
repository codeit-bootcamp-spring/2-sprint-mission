package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID userId, UUID channelId);
    List<ReadStatus> findAll();
    List<ReadStatus> findAllByUserId(UUID userId);
    void delete(UUID userId, UUID channelId);
}
