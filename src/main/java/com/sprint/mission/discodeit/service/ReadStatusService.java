package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest request);

    ReadStatus findById(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest dto);

    ReadStatus updateByChannelIdAndUserId(UUID userId, UUID channelId, ReadStatusUpdateRequest dto);

    void delete(UUID readStatusId);
}
