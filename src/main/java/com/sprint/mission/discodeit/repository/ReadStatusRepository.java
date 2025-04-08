package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    void delete(UUID readStatusKey);
    ReadStatus findByKey(UUID readStatusKey);
    List<ReadStatus> findAllByUserKey(UUID userKey);
    List<ReadStatus> findAllByChannelKey(UUID channelKey);

}
