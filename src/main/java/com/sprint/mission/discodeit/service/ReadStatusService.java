package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequestDto readStatusCreateRequestDto);
}
