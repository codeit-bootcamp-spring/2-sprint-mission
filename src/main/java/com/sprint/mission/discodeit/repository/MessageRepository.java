package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);
    Message findById(UUID messageId);
    List<Message> findAll();
    Message update(Message message);
    void delete(UUID messageId);
    boolean exists(UUID messageId);
    Instant findLatestMessageTimeByChannelId(UUID channelId);
    void deleteByChannelId(UUID channelId);
}
