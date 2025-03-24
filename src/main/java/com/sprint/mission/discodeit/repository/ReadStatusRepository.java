package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    UUID createReadStatus(ReadStatus readStatus);
    ReadStatus findById(UUID id);
    ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    void updateReadStatus(UUID id, Instant lastReadAt);
    void deleteReadStatus(UUID id);
    void deleteReadStatusByChannelId(UUID channelId);
}
