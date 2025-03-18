package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public interface ReadStatusRepository extends Repository<ReadStatus> {
    ReadStatus findByUserId(UUID userId);
    ReadStatus findByChannelId(UUID channelId);
}
