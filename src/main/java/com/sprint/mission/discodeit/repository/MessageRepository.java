package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);

  List<Message> findAllByChannelIdAndAuthorId(UUID channelId, UUID authorId);

  List<Message> findAllByAuthorId(UUID authorId);

  @EntityGraph(attributePaths = {
      "author",
      "author.profile",
      "author.status",
      "channel",
      "attachments"
  })
  List<Message> findAllByChannelId(UUID channelId);

  @EntityGraph(attributePaths = {
      "author",
      "author.profile",
      "author.status",
      "channel",
      "attachments"
  })
  Slice<Message> findByChannelId(UUID channelId, Pageable pageable);

  @EntityGraph(attributePaths = {
      "author",
      "author.profile",
      "author.status",
      "attachments",
      "channel"
  })
  Slice<Message> findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(UUID channelId,
      Instant cursor, Pageable pageable);
}
