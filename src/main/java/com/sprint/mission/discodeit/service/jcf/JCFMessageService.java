package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final List<Message> messageList;

    public JCFMessageService() {
        this.messageList = new ArrayList<>();
    }

    @Override
    public Message sendMessage(Message message) {
        messageList.add(message);
        return message;
    }

    @Override
    public Message findByMessageId(UUID messageId) {
        return messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다: " + messageId));
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageList.removeIf(message -> message.getId().equals(messageId));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return messageList.stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        return messageList.stream()
                .filter(message -> message.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}