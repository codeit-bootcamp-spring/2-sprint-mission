package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.status.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.status.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(CreateReadStatusRequest request);
    Optional<ReadStatusResponse> findById(UUID readStatusId);
    List<ReadStatusResponse> findAllByUserId(UUID userId);
    void update(UpdateReadStatusRequest request);
    void deleteById(UUID readStatusId);
}
