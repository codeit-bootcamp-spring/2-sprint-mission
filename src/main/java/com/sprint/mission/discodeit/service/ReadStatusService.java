package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void createReadStatus(CreateReadStatusRequest request);

    ReadStatus findReadStatusById(UUID id);

    List<ReadStatus> findAll();

    void updateReadStatus(UpdateReadStatusRequest request);

    void updateByUserId(UpdateReadStatusRequest request);

    void deleteReadStatus(UUID id);
}
