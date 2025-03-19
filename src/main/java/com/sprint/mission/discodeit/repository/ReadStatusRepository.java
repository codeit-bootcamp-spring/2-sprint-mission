package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public interface ReadStatusRepository {
    // 사용자와 채널 식별자로 읽음 상태를 조회
    ReadStatus findByUserAndChannel(UUID userId, UUID channelId);

    void save(ReadStatus readStatus);
}
