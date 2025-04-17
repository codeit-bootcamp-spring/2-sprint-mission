package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.request.Pageable;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Message save(Message message);

  Optional<Message> findById(UUID id);

  List<Message> findAllByChannelId(UUID getChannelId);

  @Query("""
          SELECT m FROM Message m
          JOIN FETCH m.author
          JOIN FETCH m.channel
          LEFT JOIN FETCH m.attachments
          WHERE m.channel.id = :channelId
      """)
  List<Message> findAllByChannelIdQuery(@Param("channelId") UUID channelId);


  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllByChannelId(UUID getChannelId);
}
