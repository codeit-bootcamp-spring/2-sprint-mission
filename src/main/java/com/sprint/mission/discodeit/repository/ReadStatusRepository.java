package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

    void addReadStatus(ReadStatus readStatus);

    ReadStatus findReadStatusById(UUID id);

    List<ReadStatus> findAllReadStatus();

    void deleteReadStatusById(UUID id);

    boolean existReadStatusById(UUID id);


}
