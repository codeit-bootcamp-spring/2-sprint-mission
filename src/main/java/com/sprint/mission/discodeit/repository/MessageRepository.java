package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  boolean existsByChannelId(UUID channelId);

  @EntityGraph(attributePaths = {
      "author",
      "author.profile",
      "author.status"
  })
  @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId AND m.createdAt < :createdAt")
  Slice<Message> findAllByChannelId(
      UUID channelId,
      Instant createdAt,
      Pageable pageable
  );

  void deleteAllByChannelId(UUID channelId);

  @Query("SELECT MAX(m.createdAt) FROM Message m WHERE m.channel.id = :channelId")
  Optional<Instant> findLatestCreatedAtByChannelId(UUID channelId);
}
