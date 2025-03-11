package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void register(Message message);
    List<Message> findAll();
    Optional<Message> findById(UUID messageId);
    boolean deleteMessage(UUID messageId);
    boolean updateMessage(Message message);
}
