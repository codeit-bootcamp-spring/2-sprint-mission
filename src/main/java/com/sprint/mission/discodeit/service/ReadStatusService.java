package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.create.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ReadStatusService {

    UUID create(ReadStatusCreateRequestDTO readStatusCreateRequestDTO);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    void update(UUID readStatusId, ReadStatusUpdateRequestDTO requestDTO);

    void delete(UUID readStatusId);
}
