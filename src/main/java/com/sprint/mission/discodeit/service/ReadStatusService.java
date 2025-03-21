package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(CreateReadStatusRequest request);
    Optional<ReadStatus> findById(UUID readStatusId);
    List<ReadStatus> findAllByUserId(UUID userId);
    void update(UpdateReadStatusRequest request);
    void deleteById(UUID readStatusId);
}
