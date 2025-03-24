package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;
import java.util.List;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequestDto readStatusCreateRequestDto);
    ReadStatus find(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUSerId(UUID userId);
}
