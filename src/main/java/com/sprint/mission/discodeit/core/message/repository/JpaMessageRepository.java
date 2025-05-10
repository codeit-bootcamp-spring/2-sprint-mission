package com.sprint.mission.discodeit.core.message.repository;

import com.sprint.mission.discodeit.core.message.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaMessageRepository extends JpaRepository<Message, UUID> {

  Slice<Message> findById(UUID id, Pageable pageable);
  
  @Query("SELECT m FROM Message m "
      + "LEFT JOIN FETCH m.author a "
      + "JOIN FETCH a.userStatus "
      + "LEFT JOIN FETCH a.profile "
      + "WHERE m.channel.id=:channelId AND m.createdAt < :createdAt")
  Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId,
      @Param("createdAt") Instant createdAt,
      Pageable pageable);

  @Query("SELECT m.createdAt "
      + "FROM Message m "
      + "WHERE m.channel.id = :channelId "
      + "ORDER BY m.createdAt DESC LIMIT 1")
  Optional<Instant> findLastMessageAtByChannelId(@Param("channelId") UUID channelId);

  List<Message> findByChannel_Id(UUID channelId);

  void deleteAllByChannelId(UUID channelId);
}
