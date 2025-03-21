package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponse create(ReadStatusCreateRequest request);
    Optional<ReadStatusResponse> find(UUID id);
    List<ReadStatusResponse> findAllByUserId(UUID userId);
    void update(ReadStatusUpdateRequest request);
    void delete(UUID id);
}
