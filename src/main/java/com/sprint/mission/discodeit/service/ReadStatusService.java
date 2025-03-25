package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void create(ReadStatusDto readStatusCreateDto);
    ReadStatusDto findById(UUID id);
    List<ReadStatusDto> findAllByUserId(UUID userId);
    void update(ReadStatusUpdateDto readStatusUpdateDto);
    void delete(UUID id);
}
