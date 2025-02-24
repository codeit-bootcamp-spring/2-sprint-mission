package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageData;

     public JCFMessageService() {
        messageData = new HashMap<>();
    }

    @Override
    public void create(String writingMessage, User user, Channel channel) {
         Message message = new Message(writingMessage, user, channel);
         messageData.put(message.getId(), message);
    }

    @Override
    public void delete(String userName ,String deletingMessage, Long timestamp) {
         List<Message> messages = readUser(userName);
         Message deletedMessage = messages.stream()
                 .filter(m -> m.getMessage().equals(deletingMessage))
                 .filter(m -> m.getCreatedAt().equals(timestamp))
                 .findFirst()
                 .orElse(null);
         messageData.remove(deletedMessage.getId());
    }

    @Override
    public void update(String userName, Long timestamp, String oldMessage, String newMessage) {
         //메세지 수정
        List<Message> oldMessages = readUser(userName);
        Message changedMessage =  oldMessages.stream()
                .filter(m -> m.getMessage().equals(oldMessage))
                .filter(m -> m.getCreatedAt().equals(timestamp))
                .findFirst()
                .orElse(null);
        changedMessage.updateMessage(newMessage);

    }

    @Override
    public List<Message> readUser(String userName) {
         return messageData.values().stream()
                 .filter(message -> message.getUser().getUserName().equals(userName))
                 .collect(Collectors.toList());
    }

    @Override
    public List<Message> readChannel(String channelName) {
        return messageData.values().stream()
                .filter(message -> message.getChannel().getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> readFullFilter(String userName, String channelName) {
        return messageData.values().stream()
                .filter(message -> message.getUser().getUserName().equals(userName))
                .filter(message -> message.getChannel().getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(messageData.values());
    }
}
