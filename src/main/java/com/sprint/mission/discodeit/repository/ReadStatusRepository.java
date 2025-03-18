package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    Optional<ReadStatus> findById(UUID id, UUID channelId);
    void deleteByChannelId(UUID channelId);
    ReadStatus save(ReadStatus readStatus);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    void deleteByUserIdAndChannelId(UUID userId, UUID channelId);
}
