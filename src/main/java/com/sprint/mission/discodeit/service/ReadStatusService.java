package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.dto.ReadStatusUpdateDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus createReadStatus(ReadStatusCreateDto createDto);
    ReadStatusResponseDto findById(UUID id);
    List<ReadStatusResponseDto> findAllByUserId(UUID userId);
    ReadStatus updateReadStatus(ReadStatusUpdateDto updateDto);
    void deleteReadStatus(UUID id);
}
