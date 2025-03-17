package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.ReadStatus.ReadStatusCRUDDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userID);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    void update(ReadStatus readStatus, ReadStatusCRUDDTO readStatusCRUDDTO);

    void delete(UUID readStatusId);
}
