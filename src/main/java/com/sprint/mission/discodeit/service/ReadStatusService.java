package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.readstatusdto.ReadStatusDto;
import com.sprint.mission.discodeit.application.readstatusdto.ReadStatusesDto;

import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(UUID userId, UUID channelId);

    ReadStatusesDto findByChannelId(UUID channelId);
}
