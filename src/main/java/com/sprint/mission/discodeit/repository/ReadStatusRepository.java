package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {


  @EntityGraph(attributePaths = {"channel", "user"})
  Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

  @EntityGraph(attributePaths = {"channel", "user"})
  List<ReadStatus> findAllByUserId(UUID userId);

  @EntityGraph(attributePaths = {"channel", "user"})
  List<ReadStatus> findAllByChannelId(UUID channelId);

  @EntityGraph(attributePaths = "channel")
  void deleteAllByChannelId(UUID channelId);

}
