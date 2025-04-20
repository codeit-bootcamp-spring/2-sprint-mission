package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.status.ReadStatusDto;
import com.sprint.mission.discodeit.dto.status.UpdateReadStatusRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusDto create(CreateReadStatusRequest request);

    Optional<ReadStatusDto> findById(UUID readStatusId);

    List<ReadStatusDto> findAllByUserId(UUID userId);

    ReadStatusDto update(UUID readStatusId, UpdateReadStatusRequest request);

    void deleteById(UUID readStatusId);
}
