package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final List<Message> messageList;

    public JCFMessageService() {
        this.messageList = new ArrayList<Message>();
    }

    @Override
    public Message create(Message message) {
        messageList.add(message);
        return message;
    }

    @Override
    public List<Message> findByUserId(String userId) {
        return messageList.stream()
                .filter(message -> message.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByMessageContent(String messageContent) {
        return messageList.stream()
                .filter(message -> message.getMessage().contains(messageContent))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageList);
    }

    @Override
    public Message update(String userId, Message message) {
        for (int i = 0; i < messageList.size(); i++) {
            Message existingMessage = messageList.get(i);
            if (existingMessage.getUser().getId().equals(userId)) {
                existingMessage.setMessage(message.getMessage());  // 메시지 업데이트
                return existingMessage;
            }
        }
        return null;
    }

    @Override
    public void delete(String messageContent) {
        messageList.removeIf(message -> message.getMessage().equals(messageContent));
    }
}
