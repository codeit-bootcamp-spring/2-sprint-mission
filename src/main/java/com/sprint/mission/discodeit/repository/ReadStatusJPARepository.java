package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReadStatusJPARepository extends JpaRepository<ReadStatus, UUID> {
    Boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);
    List<ReadStatus> findByUser_Id(UUID userId);
    List<ReadStatus> findByChannel_Id(UUID channelId);
}
