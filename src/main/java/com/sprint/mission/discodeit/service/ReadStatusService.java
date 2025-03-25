package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void createReadStatus(CreateReadStatusDto dto);

    ReadStatus findReadStatusById(UUID id);

    List<ReadStatus> findAll();

    void updateReadStatus(UpdateReadStatusDto dto);

    void updateByUserId(UpdateReadStatusDto dto);

    void deleteReadStatus(UUID id);
}
