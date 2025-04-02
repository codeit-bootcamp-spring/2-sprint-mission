package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    public void createReadStatus(UUID userId, UUID channelId);

    ReadStatus findReadStatusById(UUID userId, UUID channelId);

    List<ReadStatus> findAll();

    void updateReadStatus(UUID userId, UUID channelId);

    void deleteReadStatus(UUID id);
}
