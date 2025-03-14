package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus finById(UUID userId, UUID channelId);
    void save(ReadStatus readStatus);
}
