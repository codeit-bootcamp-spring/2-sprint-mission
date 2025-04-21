package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findByChannelId(UUID channelId);

  @EntityGraph(attributePaths = {"author", "author.status", "attachments"})
  Slice<Message> findSliceByChannelId(UUID channelId, Pageable pageable);

  @EntityGraph(attributePaths = {"author", "author.status", "attachments"})
  Slice<Message> findSliceByChannelIdAndCreatedAtBefore(UUID channelId, Pageable pageable,
      Instant createdAt);
}
