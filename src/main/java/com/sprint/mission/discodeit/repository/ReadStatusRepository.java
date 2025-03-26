package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findByUserId(UUID userId);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    List<UUID> findUserIdsByChannelId(UUID channelId);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
    void deleteByChannelId(UUID channelId);
    void deleteById(UUID id);
}
