package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findByUserId(UUID userUUID);

  List<ReadStatus> findByChannelId(UUID channelUUID);

  Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
}
