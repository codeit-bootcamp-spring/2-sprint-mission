package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateParam;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateParam;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void create(ReadStatusCreateParam param);

    ReadStatus find(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<UUID> findAllUserByChannelId(UUID channelId);

    List<UUID> findAllByChannelId(UUID channelId);

    void update(ReadStatusUpdateParam param);

    void delete(UUID id);
}