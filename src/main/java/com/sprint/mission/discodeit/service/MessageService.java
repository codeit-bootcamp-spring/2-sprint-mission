package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(User sender, String content, Channel channel);
    Message readMessage(UUID messageId);
    List<Message> readAllMessages();
    void updateMessageContent(UUID messageId, String content);
    void deleteMessage(UUID messageId);
    static void validateMessageId(UUID messageId, MessageRepository messageRepository) {
        if (messageId == null) {
            throw new IllegalArgumentException("입력받은 messageId 가 null 입니다!!!");
        }
        messageRepository.findMessageByMessageId(messageId);
    }
}
