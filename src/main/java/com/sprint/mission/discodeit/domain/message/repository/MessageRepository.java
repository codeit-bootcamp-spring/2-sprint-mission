package com.sprint.mission.discodeit.domain.message.repository;

import com.sprint.mission.discodeit.domain.message.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findByChannel_Id(UUID channelId);

  @Query("SELECT m FROM Message m "
      + "LEFT JOIN FETCH m.user a "
      + "LEFT JOIN FETCH a.binaryContent "
      + "WHERE m.channel.id=:channelId AND m.createdAt < :createdAt")
  Slice<Message> findAllByChannelIdWithAuthorDesc(@Param("channelId") UUID channelId,
      @Param("createdAt") Instant cursorCreatedAt, Pageable pageable);

  @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId ORDER BY m.createdAt DESC limit 1")
  Optional<Message> findLastMessageCreatedAtByChannelId(@Param("channelId") UUID channelId);

  @Query("""
      SELECT m FROM Message m  
      LEFT JOIN FETCH m.user
      WHERE m.id = :messageId
      """)
  Optional<Message> findByMessageId(UUID messageId);

  void deleteAllByChannel_Id(UUID channelId);

  @Query("""
          SELECT m
          FROM Message m
          JOIN m.attachments a
          WHERE a.id = :binaryContentId
      """)
  Optional<Message> findByAttachmentId(@Param("binaryContentId") UUID binaryContentId);

}
