package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.LinkedList;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public JCFMessageService() {
        messageRepository = new MessageRepository();
    }

    public void createMessage(Message newMessage) {
        messageRepository.addMessage(newMessage);
    }

    public Message readMessage(UUID messageId) {
        validateMessageId(messageId);
        return messageRepository.findMessageById(messageId);
    }

    public LinkedList<Message> readAllMessages() {
        return messageRepository.getMessages();
    }

    public void updateMessageContent(UUID messageId, String content) {
        validateMessageId(messageId);
        messageRepository.findMessageById(messageId).updateContent(content);
    }

    public void deleteMessage(UUID messageId) {
        messageRepository.deleteMessage(messageId);
    }

    public void validateMessageId(UUID messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("입력받은 messageId 가 null 입니다!!!");
        }
    }
}
