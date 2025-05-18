package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;

import java.time.Instant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findTop1ByChannelOrderByCreatedAtDesc(Channel channel);

  @Query("SELECT m FROM Message m "
      + "LEFT JOIN FETCH m.author a "
      + "JOIN FETCH a.status "
      + "LEFT JOIN FETCH a.profile "
      + "WHERE m.channel.id=:channelId AND m.createdAt < :createdAt")
  Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId,
      @Param("createdAt") Instant createdAt, Pageable pageable);


  void deleteByChannel_Id(UUID channelId);
}
