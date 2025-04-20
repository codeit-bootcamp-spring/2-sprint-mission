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
      value = """
    SELECT m
      FROM Message m
      LEFT JOIN FETCH m.author a
      LEFT JOIN FETCH a.profile
      JOIN FETCH a.userStatus
     WHERE m.channel.id    = :channelId
       AND m.createdAt     < :createdAt
     ORDER BY m.createdAt DESC
  """,
      countQuery = """
    SELECT COUNT(m)
      FROM Message m
     WHERE m.channel.id    = :channelId
       AND m.createdAt     < :createdAt
  """)
  Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId, @Param("createdAt") Instant createdAt, Pageable pageable);

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
