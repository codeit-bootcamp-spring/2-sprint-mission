package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final List<Message> messageList;

    public JCFMessageService() {
        this.messageList = new ArrayList<>();
    }

    @Override
    public Message create(Message message) {
        if (message.getUser() == null || message.getChannel() == null) {
            throw new IllegalArgumentException("메시지를 보내는 사용자와 채널은 반드시 존재해야 합니다.");
        }
        if (!message.getChannel().getUsers().contains(message.getUser())) {
            throw new IllegalArgumentException("메시지를 보내는 사용자가 채널에 속해있지않습니다.");
        }
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
    public List<Message> findByChannel(Channel channel) {
        return messageList.stream()
                .filter(message -> message.getChannel().equals(channel))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String messageContent) {
        messageList.removeIf(message -> message.getMessage().equals(messageContent));
    }
}