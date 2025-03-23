package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository {
    ReadStatus create(ReadStatusCreate dto);
    List<ReadStatus> findAll();
    void delete(UUID readStatusID);
}
