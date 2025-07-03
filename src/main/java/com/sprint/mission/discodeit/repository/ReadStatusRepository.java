package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatus> findAllByUserId(UUID userId);

  @EntityGraph(attributePaths = {"user"})
  List<ReadStatus> findAllByChannelId(UUID channelId);

  void deleteAllByChannelId(UUID channelId);

  @Query("SELECT r.user.id FROM ReadStatus r WHERE r.id = :id")
  Optional<UUID> findUserIdById(UUID id);

}
