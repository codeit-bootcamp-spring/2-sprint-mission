package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID userId, UUID channelId, String message);
    Message readMessage(UUID id);
    List<Message> readAllMessages();
    Message updateMessage(UUID id, Message message);
    void deleteMessage(UUID id);
}