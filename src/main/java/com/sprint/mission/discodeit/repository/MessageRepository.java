package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @NonNull
    Message save(@NonNull Message message);

    @NonNull
    Optional<Message> findById(@NonNull UUID id);

    Page<Message> findAllByChannelId(UUID channelId, Pageable pageable);


    boolean existsById(@NonNull UUID id);

    void deleteById(@NonNull UUID id);

    void deleteAllByChannelId(UUID channelId);

    Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);
}
