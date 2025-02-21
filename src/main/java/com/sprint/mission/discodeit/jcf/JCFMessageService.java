package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messages = new HashMap<UUID, Message>();
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void createMessage(String userName, String channelName, String content) {
        userService.validateUserExists(userName);
        channelService.validateChannelExists(channelName);

        Message message = new Message(userName, channelName, content);
        messages.put(message.getUuid(), message);
    }

    @Override
    public Message getMessageById(UUID messageId) {
        if(!messages.containsKey(messageId)){
            throw new NoSuchElementException("존재하지 않는 메세지ID 입니다.");
        }
        return messages.get(messageId);
    }

    @Override
    public List<Message> getMessagesByUserAndChannel(String userName, String channelName) {
        userService.validateUserExists(userName);
        channelService.validateChannelExists(channelName);

        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getSender().equals(userName) && message.getChannel().equals(channelName)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public List<Message> getChannelMessages(String channelName) {
        channelService.validateChannelExists(channelName);

        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getChannel().equals(channelName)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public List<Message> getUserMessages(String userName) {
        userService.validateUserExists(userName);

        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getSender().equals(userName)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        if(!messages.containsKey(messageId)){
            throw new NoSuchElementException("존재하지 않는 메세지ID 입니다.");
        }
        Message message = messages.get(messageId);
        message.updateContent(newContent);
        messages.put(messageId, message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        if(!messages.containsKey(messageId)){
            throw new NoSuchElementException("존재하지 않는 메세지ID 입니다.");
        }
        messages.remove(messageId);
    }
}
