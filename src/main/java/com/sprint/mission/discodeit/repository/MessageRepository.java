package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @NonNull
    Message save(@NonNull Message message);

    @NonNull
    Optional<Message> findById(@NonNull UUID id);

    boolean existsById(@NonNull UUID id);

    void deleteById(@NonNull UUID id);

    void deleteAllByChannelId(UUID channelId);

    Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);

    List<Message> findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(UUID channelId,
        Instant createdAt, Pageable pageable);

    List<Message> findByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);

    List<Message> findByAuthorId(UUID authorId);
}
