package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusFindResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    UUID createReadStatus(ReadStatusCreateRequest readStatusCreateRequest);
    ReadStatusFindResponse findReadStatus(UUID readStatusId);
    List<ReadStatusFindResponse> findAllReadStatusByUserId(UUID userId);
    void updateReadStatus(ReadStatusUpdateRequest readStatusUpdateRequest);
    void deleteReadStatus(UUID readStatusId);
}
