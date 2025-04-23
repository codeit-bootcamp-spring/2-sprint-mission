package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByChannelId(UUID channelId);

  void deleteAllByChannelId(UUID channelId);

  @Query("SELECT m.createdAt FROM Message m WHERE m.channel.id = :channelId ORDER BY m.createdAt DESC LIMIT 1")
  Optional<Instant> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);
}
