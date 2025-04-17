package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDto;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(ReadStatusCreateDto readStatusCreateDto);

    ReadStatusDto findById(UUID readStatusId);

    List<ReadStatusDto> findAll();

    List<ReadStatusDto> findAllByUserId(UUID userId);

    List<ReadStatusDto> findAllByChannelId(UUID channelId);

    ReadStatusDto update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto);

    void delete(UUID readStatusId);
}
