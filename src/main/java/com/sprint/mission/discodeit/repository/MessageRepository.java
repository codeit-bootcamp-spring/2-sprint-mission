package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @EntityGraph(attributePaths = {"author", "channel"})
  List<Message> findAll();

  @EntityGraph(attributePaths = {"author", "channel"})
  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);

  @EntityGraph(attributePaths = {"author", "channel"})
  List<Message> findAllByChannelIdAndAuthorId(UUID channelId, UUID authorId);

  @EntityGraph(attributePaths = {"author", "channel"})
  List<Message> findAllByAuthorId(UUID authorId);

  @EntityGraph(attributePaths = {"author", "channel"})
  List<Message> findAllByChannelId(UUID channelId);

  @EntityGraph(attributePaths = {"author", "channel"})
  Slice<Message> findByChannelId(UUID channelId, Pageable pageable);

  void deleteAllByChannelId(UUID channelId);
}
