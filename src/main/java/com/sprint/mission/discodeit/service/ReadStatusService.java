package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    UUID create(ReadStatusCreateRequest request);
    ReadStatus find(UUID readStatusId);
    List<ReadStatus> findAllByUserId(UUID userId);
    void update(ReadStatusUpdateRequest request);
    void delete(UUID readStatusId);
}