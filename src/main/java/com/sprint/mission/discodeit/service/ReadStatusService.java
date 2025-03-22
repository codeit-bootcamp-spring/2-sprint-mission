package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.readstatusdto.ReadStatusesDto;

import java.util.UUID;

public interface ReadStatusService {
    ReadStatusesDto findByChannelId(UUID channelId);
}
