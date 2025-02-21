package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(Message newMessage);
    Message readMessage(UUID messageId);
    List<Message> readAllMessages();
    void updateMessageContent(UUID messageId, String content);
    void deleteMessage(UUID messageId);
}
