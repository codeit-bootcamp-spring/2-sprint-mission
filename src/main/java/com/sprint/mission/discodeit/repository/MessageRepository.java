package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.message.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    Message findById(UUID messageId);

    List<Message> findAll();

    List<Message> findBySenderId(UUID senderId);

    Message update(UUID messageId, String content);

    boolean delete(UUID messageId);
}
