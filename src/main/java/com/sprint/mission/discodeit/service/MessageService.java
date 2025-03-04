package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface MessageService {
    void createMessage(User sender, String content, Channel channel);
    Message readMessage(UUID messageId);
    Map<UUID, Message> readAllMessages();
    void updateMessageContent(UUID messageId, String content);
    void deleteMessage(UUID messageId);
    static void validateMessageId(UUID messageId, MessageRepository messageRepository) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("해당 messageId를 가진 사용자를 찾을 수 없습니다 : " + messageId);
        }
    }
}
