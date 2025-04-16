package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.dto.readstatusdto.*;


import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusResponseDto create(ReadStatusCreateDto readStatusCreateDto);
    ReadStatusResponseDto find(ReadStatusFindDto readStatusFindDto);
    List<ReadStatusResponseDto> findAllByUserId(UUID userId);
    ReadStatusResponseDto update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto);
    void delete(ReadStatusDeleteDto readStatusDeleteDto);
}
