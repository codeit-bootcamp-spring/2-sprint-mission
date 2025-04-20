package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChannel_Id(UUID channelId);

    @Query("SELECT m.createdAt FROM Message m WHERE m.channel.id = :channelId ORDER BY m.createdAt DESC")
    Optional<Instant> findLastMessageCreatedAtByChannelId(@Param("channelId") UUID channelId);
}
