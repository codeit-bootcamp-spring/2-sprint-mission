package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private static Map<UUID, Message> messageData;
    private final UserService userService;
    private final ChannelService channelService;

     public JCFMessageService(UserService userService, ChannelService channelService) {
        messageData = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }


    @Override
    public void create(String writingMessage, User user, Channel channel) {
        User validUser = userService.read(user.getUserName());

        Message message = new Message(writingMessage, validUser, channel);
        messageData.put(message.getId(), message);
    }

    @Override
    public void delete(String userName ,String deletingMessage, Long timestamp) {
        User validUser = userService.read(userName);
        if (validUser == null) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        List<Message> messages = readUser(userName);
        Message deletedMessage = messages.stream()
                .filter(m -> m.getMessage().equals(deletingMessage))
                .filter(m -> m.getUpdatedAt().equals(timestamp))
                .findFirst()
                .orElse(null);
        if (deletedMessage == null) {
            throw new IllegalArgumentException("삭제할 메시지를 찾을 수 없습니다.");
        }
        messageData.remove(deletedMessage.getId());
    }

    @Override
    public void update(String userName, Long timestamp, String oldMessage, String newMessage) {
         //메세지 수정
        User validUser = userService.read(userName);
        if (validUser == null) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        List<Message> oldMessages = readUser(userName);
        Message changedMessage = oldMessages.stream()
                .filter(m -> m.getMessage().equals(oldMessage))
                .filter(m -> m.getUpdatedAt().equals(timestamp))
                .findFirst()
                .orElse(null);
        if (changedMessage == null) {
            throw new IllegalArgumentException("수정할 메시지를 찾을 수 없습니다.");
        }
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
