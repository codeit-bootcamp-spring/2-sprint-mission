package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findAllByChannel(Channel channel);

    @Query("SELECT m FROM Message m WHERE m.channel = :channel ORDER BY m.createdAt DESC")
    List<Message> findFirstPageByChannel(@Param("channel") Channel channel, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.channel = :channel AND m.createdAt < :cursor ORDER BY m.createdAt DESC")
    List<Message> findNextPageByChannelAndCursor(@Param("channel") Channel channel,
        @Param("cursor") Instant cursor, Pageable pageable);
}
