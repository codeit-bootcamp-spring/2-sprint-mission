package com.sprint.mission.discodeit.core.status.repository;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReadStatusRepository extends JpaRepository<ReadStatus, UUID> {


  ReadStatus findByUser_Id(UUID userId);

  ReadStatus findByChannel_Id(UUID channelId);

  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findAllByUser_Id(UUID userId);

  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findAllByChannel_Id(UUID channelId);
}
