package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository {
    Message save(Message message);
    List<Message> findAllByChannelId(UUID channelId);
    Optional<Message> findById(UUID messageId);
    boolean existsById(UUID messageId);
    void deleteById(UUID messageId);
}