package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID userId, UUID channelId, String text);
    Optional<Message> getMessageById(UUID messageId);
    List<Message> getAllMessagesByChannel(UUID channelId);
    void updateMessage(UUID messageId, String newText);
    void deleteMessage(UUID messageId);
}
