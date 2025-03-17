package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    ReadStatus findById(UUID userId, UUID channelId);
    void deleteByChannelId(UUID channelId);
    void save(ReadStatus readStatus);
}
