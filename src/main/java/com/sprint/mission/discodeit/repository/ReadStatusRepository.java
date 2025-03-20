package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findByUserId(UUID userId);
    List<ReadStatus> findByChannelId(UUID channelId);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    List<ReadStatus> findAll();
    void deleteById(UUID id);
    void deleteByUserId(UUID userId);
    void deleteByChannelId(UUID channelId);
    List<UUID> findUserIdsByChannelId(UUID channelId);
    boolean existsByUserIdAndChannelId(UUID userId, UUID id);
}
