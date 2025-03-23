package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    List<ReadStatus> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);

    // 특정 채널에 해당하는 모든 ReadStatus 조회
    // 해당 채널의 모든 ReadStatus 삭제
    List<ReadStatus> findByChannelId(UUID channelId);
    void deleteByChannelId(UUID channelId);
}
