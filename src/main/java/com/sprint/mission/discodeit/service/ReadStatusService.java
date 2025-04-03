package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusFindDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusUpdateDto;


import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatus create(ReadStatusCreateDto readStatusCreateDto);
    ReadStatus find(ReadStatusFindDto readStatusFindDto);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto);
    void delete(ReadStatusDeleteDto readStatusDeleteDto);
}
