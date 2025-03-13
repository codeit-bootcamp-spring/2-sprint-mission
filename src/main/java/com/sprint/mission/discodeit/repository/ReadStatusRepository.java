package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    List<ReadStatus> findAll();

    ReadStatus findById(UUID userId);

    void delete(UUID userId);
}
