package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.DTO.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userID);

    void update(ReadStatus readStatus,ReadStatusUpdateDTO readStatusUpdateDTO);

    void delete(UUID readStatusId);
}
