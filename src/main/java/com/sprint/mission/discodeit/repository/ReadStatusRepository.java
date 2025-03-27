package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    ReadStatus find(UUID id);
    List<ReadStatus> findAll();
    List<ReadStatus> findAllByChannelId(UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID id);
    void delete(UUID id);
    void deleteAllByChannelId(UUID channelId);
}
