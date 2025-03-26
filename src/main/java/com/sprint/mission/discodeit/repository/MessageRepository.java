package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findAllByChannelId(UUID channelId);
    void deleteById(UUID id);
    void deleteByChannelId(UUID channelId);
    Optional<Instant> findLatestMessageTimeByChannelId(UUID channelId);
}
