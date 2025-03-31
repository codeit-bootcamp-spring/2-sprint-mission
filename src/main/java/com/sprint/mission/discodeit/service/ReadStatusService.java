package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateReadStatusDTO;
import com.sprint.mission.discodeit.dto.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(CreateReadStatusDTO createReadStatusDTO);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId);
    void update(UpdateReadStatusDTO updateReadStatusDTO);
    void delete(UUID id);
}
