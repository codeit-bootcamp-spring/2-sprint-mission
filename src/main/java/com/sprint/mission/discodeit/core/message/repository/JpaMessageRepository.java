package com.sprint.mission.discodeit.core.message.repository;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageRepository extends JpaRepository<Message, UUID> {

  Slice<Message> findById(UUID id, Pageable pageable);

  @EntityGraph(attributePaths = {"author", "channel", "attachment"})
  List<Message> findAllByChannel_Id(UUID channelId);

  @EntityGraph(attributePaths = {"author", "channel", "attachment"})
  Slice<Message> findAllByChannel_Id(UUID channelId, Pageable pageable);

  @EntityGraph(attributePaths = {"author", "channel", "attachment"})
  Slice<Message> findAllByChannel_IdAndCreatedAtLessThanOrderByCreatedAt(UUID channelId,
      Instant createdAtIsLessThan, Pageable pageable);
}
