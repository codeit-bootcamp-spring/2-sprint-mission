package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusesDto;

import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(UUID userId, UUID channelId);

    ReadStatusesDto findByChannelId(UUID channelId);

    ReadStatusDto find(UUID readStatusId);

    ReadStatusesDto findAllByUserId(UUID useId);

    ReadStatusDto updateLastReadTime(UUID readStatusId);

    void delete(UUID readStatusId);
}
