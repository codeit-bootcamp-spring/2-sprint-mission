package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findByUserId(UUID userId);
    //    List<ReadStatus> findByChannelId(UUID channelId);
    List<ReadStatus> findAll();
    void delete(UUID id);
}
