package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message sendMessage(String content, UUID userId, UUID channelId);
    List<Message> findAllMessages();
    Message findByMessageId(UUID messageId);
    Message updateMessage(UUID messageId, String newContent);
    void deleteMessage(UUID messageId);
}
