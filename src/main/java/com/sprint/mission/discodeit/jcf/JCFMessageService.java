package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messages = new HashMap<UUID, Message>();
    private final UserService userService;
    private final ChannelService channelService;
    private static JCFMessageService INSTANCE;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    public static synchronized  JCFMessageService getInstance(UserService userService, ChannelService channelService){
        if(INSTANCE == null){
            INSTANCE = new JCFMessageService(userService, channelService);
        }
        return INSTANCE;
    }

    @Override
    public void createMessage(UUID userId, UUID channelId, String content) {
        userService.validateUserExists(userId);
        channelService.validateChannelExists(channelId);

        Message message = new Message(userId, channelId, content);
        channelService.addMessageToChannel(channelId, message.getId());
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessageById(UUID messageId) {
        validateMessage(messageId);
        return messages.get(messageId);
    }

    @Override
    public List<Message> getMessagesByUserAndChannel(UUID userId, UUID channelId) {
        userService.validateUserExists(userId);
        channelService.validateChannelExists(channelId);

        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getSenderId().equals(userId) && message.getChannelId().equals(channelId)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public List<Message> getChannelMessages(UUID channelId) {
        channelService.validateChannelExists(channelId);
        Channel channel = channelService.getChannelById(channelId);

        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getChannelId().equals(channelId)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public List<Message> getUserMessages(UUID userId) {
        userService.validateUserExists(userId);

        List<Message> messages = new ArrayList<>();
        for (Message message : this.messages.values()) {
            if (message.getSenderId().equals(userId)) {
                messages.add(message);
            }
        }

        return messages;
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        validateMessage(messageId);
        Message message = messages.get(messageId);
        message.updateContent(newContent);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        validateMessage(messageId);

        Message message = messages.get(messageId);
        channelService.removeMessageFromChannel(message.getChannelId(), messageId);
        messages.remove(messageId);
    }

    @Override
    public void validateMessage(UUID messageId) {
        if(!messages.containsKey(messageId)){
            throw new NoSuchElementException("존재하지 않는 메세지 입니다.");
        }
    }
}
