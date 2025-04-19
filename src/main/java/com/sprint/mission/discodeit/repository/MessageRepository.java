package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @Query(
      "select m from Message m "
          + "left join m.author a "
          + "left join fetch a.profile "
          + "join fetch a.userStatus "
          + "where m.channel.id = :channelId and m.createdAt < :cursor")
  Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId, @Param("cursor") Instant cursor, Pageable pageable);

  @Query(
      "select m from Message m "
          + "left join fetch m.author a "
          + "join fetch a.userStatus "
          + "left join fetch a.profile "
          + "left join fetch m.attachments "
          + "where m.id = :id")
  Optional<Message> findByIdWithAuthorAndAttachments(@Param("id") UUID id);

  @Query("select max(m.createdAt) from Message m where m.channel.id = :channelId")
  Instant findLastMessageTimeByChannelId(@Param("channelId") UUID channelId);

  void deleteAllByChannelId(UUID channelId);
}
