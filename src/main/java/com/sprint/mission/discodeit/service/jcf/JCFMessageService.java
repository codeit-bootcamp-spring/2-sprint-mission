package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFMessageService implements MessageService {
    private final ConcurrentHashMap<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new ConcurrentHashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String message) {
        if(userService.readUser(userId) == null) {
            throw new IllegalArgumentException("User not found");
        }
        if(channelService.readChannel(channelId) == null) {
            throw new IllegalArgumentException("Channel not found");
        }
        Message newMessage = new Message(userId,channelId,message);
        data.put(newMessage.getId(), newMessage);
        return newMessage;
    }

    @Override
    public Message readMessage(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message updateMessage(UUID id, Message message) {
        if(data.containsKey(id)) {
            Message existingMessage = data.get(id);
            if(userService.readUser(existingMessage.getUserId()) == null) {
                throw new IllegalArgumentException("User not found");
            }
            if(channelService.readChannel(existingMessage.getChannelId()) == null) {
                throw new IllegalArgumentException("Channel not found");
            }
            existingMessage.update(message.getMessage());
            return existingMessage;
        }
        return null;
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
