package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository {
    ReadStatus create(ReadStatusCreate dto);
    ReadStatus findById(UUID id);
    List<ReadStatus> findAll();
    void delete(UUID readStatusID);
    ReadStatus save(ReadStatus readStatus);
    Map<UUID, ReadStatus> getReadStatusData();
    ReadStatus update(UUID id);
}
