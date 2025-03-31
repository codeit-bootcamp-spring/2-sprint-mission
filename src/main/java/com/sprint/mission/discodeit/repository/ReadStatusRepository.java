package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

    Optional<ReadStatus> find(UUID id);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    void delete(UUID id);

    boolean existsById(UUID id);
}
