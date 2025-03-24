package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findByUserId(UUID userId);
    List<ReadStatus> findByChannelId(UUID channelId);
    void deleteById(UUID id);
}
