package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findAllByChannelId(UUID channelId);
    boolean existsById(UUID id);
    void deleteByMessageId(UUID id);
    void deleteByChannelId(UUID channelId);
    Optional<Instant> findLatestMessageTimeByChannelId(UUID channelId);
}
