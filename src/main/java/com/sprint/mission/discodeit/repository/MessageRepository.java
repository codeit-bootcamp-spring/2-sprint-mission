package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  Slice<Message> findAllByChannelIdAndCreatedAtLessThan(UUID channelId, Instant cratedAt,
      Pageable pageable);

  Optional<Message> findFirstByChannelIdOrderByCreatedAtDesc(UUID channelId);

  void deleteAllByChannelId(UUID channelId);
}
