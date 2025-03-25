package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void createReadStatus(ReadStatusCreateDto dto);

    ReadStatus findReadStatusById(UUID id);

    List<ReadStatus> findAll();

    void updateReadStatus(ReadStatusUpdateDto dto);

    void updateByUserId(ReadStatusUpdateDto dto);

    void deleteReadStatus(UUID id);
}
