package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusResult;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResult create(ReadStatusCreateRequest request);

    List<ReadStatusResult> getByChannelId(UUID channelId);

    ReadStatusResult getByReadStatusId(UUID readStatusId);

    List<ReadStatusResult> getAllByUserId(UUID userId);

    ReadStatusResult updateLastReadTime(UUID readStatusId);

    void delete(UUID readStatusId);
}
