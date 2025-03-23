package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus createReadStatus(ReadStatusDTO readStatusDTO);
    ReadStatus getReadStatus(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    void updateReadStatus(UUID userId, UUID channelId, ReadStatusDTO readStatusDTO);
    void deleteReadStatus(UUID userId, UUID channelId);
}