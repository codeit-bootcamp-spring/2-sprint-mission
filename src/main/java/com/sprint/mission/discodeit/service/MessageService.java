package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(CreateMessageRequest request);
    Optional<Message> getMessageById(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    void updateMessage(UUID messageId, String newText);
    void deleteMessage(UUID messageId);
}
