package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateDto readStatusCreateDto);

    ReadStatus findById(UUID readStatusId);

    List<ReadStatus> findAll();

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    ReadStatus update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdateDto);

    void delete(UUID readStatusId);
}
