package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

public interface MessageRepository extends JpaRepository<Message, UUID> {


    Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);

    List<Message> findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(UUID channelId,
        Instant createdAt, Pageable pageable);

    List<Message> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);

    List<Message> findByAuthorId(UUID authorId);

    @Modifying
    @Transactional
    void deleteAllByChannelId(UUID channelId);
}
