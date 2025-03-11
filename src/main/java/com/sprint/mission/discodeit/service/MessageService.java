package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID senderId, String content, UUID channelId);
    Message readMessage(UUID messageId);
    Map<UUID, Message> readAllMessages();
    void updateMessageContent(UUID messageId, String newContent);
    void deleteMessage(UUID messageId);
    static void validateMessageId(UUID messageId, MessageRepository jcfMessageRepository) {
        if (!jcfMessageRepository.existsById(messageId)) {
            throw new NoSuchElementException("해당 messageId를 가진 사용자를 찾을 수 없습니다 : " + messageId);
        }
    }
}
