package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.request.readstatusdto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.request.readstatusdto.ReadStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.request.readstatusdto.ReadStatusFindDto;
import com.sprint.mission.discodeit.service.dto.request.readstatusdto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusResponseDto;


import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusResponseDto create(ReadStatusCreateDto readStatusCreateDto);
    ReadStatusResponseDto find(ReadStatusFindDto readStatusFindDto);
    List<ReadStatusResponseDto> findAllByUserId(UUID userId);
    ReadStatusResponseDto update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto);
    void delete(ReadStatusDeleteDto readStatusDeleteDto);
}
