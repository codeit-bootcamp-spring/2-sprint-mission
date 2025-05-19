package com.sprint.mission.discodeit.domain.message.repository;

import com.sprint.mission.discodeit.domain.message.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByChannel_Id(UUID channelId);

    @Query("SELECT m FROM Message m "
            + "LEFT JOIN FETCH m.user a "
            + "JOIN FETCH a.userStatus "
            + "LEFT JOIN FETCH a.binaryContent "
            + "WHERE m.channel.id=:channelId AND m.createdAt < :createdAt")
    Slice<Message> findAllByChannelIdWithAuthorDesc(@Param("channelId") UUID channelId, @Param("createdAt") Instant cursorCreatedAt, Pageable pageable);

    @Query("SELECT m.createdAt FROM Message m WHERE m.channel.id = :channelId ORDER BY m.createdAt DESC limit 1")
    Optional<Instant> findLastMessageCreatedAtByChannelId(@Param("channelId") UUID channelId);

    void deleteAllByChannel_Id(UUID channelId);

}
