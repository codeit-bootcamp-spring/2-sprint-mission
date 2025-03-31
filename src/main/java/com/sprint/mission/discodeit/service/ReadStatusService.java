package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequestDto dto);
    ReadStatus findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(ReadStatusUpdateRequestDto dto);
    void delete(UUID id);
}
