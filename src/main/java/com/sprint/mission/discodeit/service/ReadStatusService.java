package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatusService.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusService.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest);
    ReadStatus findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID id);
    ReadStatus update(ReadStatusUpdateRequest readStatusUpdateRequest);
    void delete(UUID id);
}
