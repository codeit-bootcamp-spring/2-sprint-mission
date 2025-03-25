package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void create(ReadStatusCreateRequest param);

    ReadStatus find(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<UUID> findAllUserByChannelId(UUID channelId);

    List<UUID> findAllByChannelId(UUID channelId);

    void update(ReadStatusUpdateRequest param);

    void delete(UUID id);
}