package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID userId, UUID channelId);
    List<Message> findAll();
    Message findByMessageId(UUID messageId);
    Message update(UUID messageId, String newContent);
    void delete(UUID messageId);
}
