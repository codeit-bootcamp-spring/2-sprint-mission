package com.sprint.mission.discodeit.domain.readstatus.repository;

import com.sprint.mission.discodeit.domain.readstatus.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    List<ReadStatus> findByChannel_Id(UUID channelId);

    List<ReadStatus> findByUser_Id(UUID userId);

    boolean existsByChannel_IdAndUser_Id(UUID channelId, UUID userId);

    void deleteAllByChannel_Id(UUID channelId);

}
