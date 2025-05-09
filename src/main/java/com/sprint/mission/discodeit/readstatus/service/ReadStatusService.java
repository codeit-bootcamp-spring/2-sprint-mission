package com.sprint.mission.discodeit.readstatus.service;

import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResult;
import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusCreateRequest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResult create(ReadStatusCreateRequest request);

    List<ReadStatusResult> getAllByUserId(UUID userId);

    ReadStatusResult updateLastReadTime(UUID readStatusId);

    void delete(UUID readStatusId);
}
