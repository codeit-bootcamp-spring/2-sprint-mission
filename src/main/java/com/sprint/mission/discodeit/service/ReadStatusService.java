package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusResult;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResult create(UUID userId, UUID channelId);

    List<ReadStatusResult> findByChannelId(UUID channelId);

    ReadStatusResult findByReadStatusId(UUID readStatusId);

    List<ReadStatusResult> findAllByUserId(UUID useId);

    ReadStatusResult updateLastReadTime(UUID readStatusId);

    void delete(UUID readStatusId);
}
