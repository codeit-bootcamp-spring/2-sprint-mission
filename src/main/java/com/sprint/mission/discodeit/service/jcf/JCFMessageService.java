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
    private final Map<UUID, Message> messageData;
    private final UserService userService;
    private final ChannelService channelService;

     public JCFMessageService(UserService userService, ChannelService channelService) {
        messageData = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    public UUID getUserId(String userName) {
         User user = userService.read(userName);
         if(user == null){
             throw new IllegalArgumentException("유효하지 않은 사용자명입니다.");
         }
         return user.getId();
    }

    public UUID getChannelId(String channelName) {
         Channel channel = channelService.read(channelName);
         if(channel == null){
             throw new IllegalArgumentException("유효하지 않은 채널명입니다.");
         }
         return channel.getId();
    }




    @Override
    public Message create(String writingMessage, User user, Channel channel) {
         if(user == null || channel == null){
             throw new IllegalArgumentException("유저와 채널을 할당해주세요.");
         }
         Message message = new Message(writingMessage, user, channel);
         messageData.put(message.getId(), message);
         System.out.printf("(%d)[%s]%s: %s%n",message.getUpdatedAt(), user.getUserName()
                 , user.getUserName(), writingMessage);
         return message;
    }

    @Override
    public void delete(UUID messageId) {
         Message message = messageData.get(messageId);
         if (message == null) {
             throw new IllegalArgumentException("삭제할 메시지를 찾을 수 없습니다.");
         }
         messageData.remove(messageId);
    }

    @Override
    public void update(UUID messageId, String newMessage) {
         Message message = messageData.get(messageId);
         if (message == null) {
             throw  new IllegalArgumentException("수정할 메세지를 찾을 수 없습니다.");
         }
         message.updateMessage(newMessage);
    }

    @Override
    public List<Message> readUser(String userName) {
         UUID userId = getUserId(userName);
         return messageData.values().stream()
                 .filter(message -> message.getUser().getId().equals(userId))
                 .collect(Collectors.toList());
    }

    @Override
    public List<Message> readChannel(String channelName) {
        UUID channelId = getChannelId(channelName);
         return messageData.values().stream()
                .filter(message -> message.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> readFullFilter(String userName, String channelName) {
         UUID userId = getUserId(userName);
         UUID channelId = getChannelId(channelName);
        return messageData.values().stream()
                .filter(message -> message.getUser().getId().equals(userId))
                .filter(message -> message.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> readAll() {
        return messageData.values().stream().toList();
    }
}
