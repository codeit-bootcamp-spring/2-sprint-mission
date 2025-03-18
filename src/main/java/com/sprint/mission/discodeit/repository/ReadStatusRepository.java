package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;

public interface ReadStatusRepository {
    Optional<ReadStatus> findByUserAndChannel(UUID userId, UUID channelId);
    void save(ReadStatus readStatus); //전체 업데이트(readStatus 생성)
    void updateLastReadAt(UUID userId, UUID channelId, Instant newLastReadAt); //부분 업데이트(이미 readStatus가 존재한다면)
    void delete(UUID userId, UUID channelId);
}
