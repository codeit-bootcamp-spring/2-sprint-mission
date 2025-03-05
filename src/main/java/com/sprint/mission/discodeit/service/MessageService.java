package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message);
    Message findById(UUID messageId);
    List<Message> findAll();
    void delete(UUID messageId);
    void update(UUID messageId, String content);
}
