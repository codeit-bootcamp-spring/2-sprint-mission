package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends Repository<ReadStatus> {
    List<ReadStatus> findByUserId(UUID userId);
    List<ReadStatus> findByChannelId(UUID channelId);
    void updateReadTime(UUID readStatusId, Instant readTime);
    void deleteByUserId(UUID userId);
    void deleteByChannelId(UUID channelId);
}
