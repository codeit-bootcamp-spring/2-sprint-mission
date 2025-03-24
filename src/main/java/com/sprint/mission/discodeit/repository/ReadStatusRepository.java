package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;

public interface ReadStatusRepository {

    ReadStatus save(ReadStatus readStatus);
    List<ReadStatus> load();
    void remove(ReadStatus readStatus);

}
