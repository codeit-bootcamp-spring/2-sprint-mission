package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save();
    ReadStatus find(UUID id);
    List<ReadStatus> findAll();
    ReadStatus update(ReadStatus readStatus);
    void delete();
}
