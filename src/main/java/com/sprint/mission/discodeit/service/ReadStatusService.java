package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusParam;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusParam;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDTO create(CreateReadStatusParam createReadStatusParam);
    ReadStatusDTO find(UUID id);
    List<ReadStatusDTO> findAllByUserId(UUID userId);
    List<ReadStatusDTO> findAllByChannelId(UUID channelId);
    UpdateReadStatusDTO update(UpdateReadStatusParam updateReadStatusParam);
    void delete(UUID id);
    void deleteByChannelId(UUID channelId);
}
