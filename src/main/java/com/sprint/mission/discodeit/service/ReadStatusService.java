package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.create.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ReadStatusService {

    ReadStatus create(UUID userId, UUID channelId, ReadStatusCreateRequestDTO readStatusCreateRequestDTO);

    ReadStatus find(UUID readStatusId);

    ReadStatus findByUserId(UUID userId);

    List<ReadStatus> findAllByUserId(UUID userId);

    ReadStatus update(UUID channelId, ReadStatusUpdateRequestDTO requestDTO);

    void delete(UUID readStatusId);
}
