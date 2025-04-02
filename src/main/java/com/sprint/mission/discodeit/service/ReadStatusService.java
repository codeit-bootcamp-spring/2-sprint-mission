package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusDto;
import com.sprint.mission.discodeit.DTO.ReadStatus.UpdateReadStatusDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(CreateReadStatusDto dto);
    ReadStatusDto find(UUID id);
    List<ReadStatusDto> findAllByUserId(UUID userId);
    ReadStatusDto update(UpdateReadStatusDto dto);
    void delete(UUID id);
}
