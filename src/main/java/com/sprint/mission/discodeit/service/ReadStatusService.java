package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequestDTO dto);
    Optional<ReadStatus> find(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(ReadStatusUpdateRequestDTO dto);
    void delete(UUID userId, UUID channelId);
}
