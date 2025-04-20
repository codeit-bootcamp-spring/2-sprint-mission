package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @EntityGraph(attributePaths = {"attachments"})
    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

    List<Message> findAllByChannelId(UUID channelId);

    @EntityGraph(attributePaths = {"attachments"})
    Slice<Message> findAllByAuthorId(UUID authorId, Pageable pageable);

    List<Message> findAllByAuthorId(UUID authorId);

    @Query("SELECT MAX(m.createdAt) FROM Message m WHERE m.channel.id = :channelId")
    Instant findLastMessageAtByChannelId(@Param("channelId") UUID channelId);
}
