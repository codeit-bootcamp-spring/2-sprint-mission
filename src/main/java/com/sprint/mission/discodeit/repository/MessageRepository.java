package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);
    Optional<Message> findById(UUID messageId);
    List<Message> findAll();
    void delete(UUID messageId);
    void update(UUID messageId, String content);
}
