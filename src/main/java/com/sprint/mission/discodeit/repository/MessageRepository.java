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

    // 즉시로딩 및 FETCHJOIN을 통해 N+1문제 방지
    @Query("SELECT m FROM Message m "
        + "LEFT JOIN FETCH m.author a "
        + "JOIN FETCH a.status "
        + "LEFT JOIN FETCH a.profile "
        // 커서기반 페이지네이션 구현 가능
        + "WHERE m.channel.id=:channelId AND m.createdAt < :createdAt")
    Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId,
        @Param("createdAt") Instant createdAt,
        Pageable pageable);


    @Query("SELECT m.createdAt "
        + "FROM Message m "
        + "WHERE m.channel.id = :channelId "
        + "ORDER BY m.createdAt DESC LIMIT 1")
    Optional<Instant> findLastMessageAtByChannelId(@Param("channelId") UUID channelId);

    void deleteAllByChannelId(UUID channelId);
}
