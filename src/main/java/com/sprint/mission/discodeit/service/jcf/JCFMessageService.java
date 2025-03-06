package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private static JCFMessageService INSTANCE;
    private final Map<UUID, Message> messages = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    public static synchronized JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (INSTANCE == null) {
            INSTANCE = new JCFMessageService(userService, channelService);
        }
        return INSTANCE;
    }

    @Override
    public Message createMessage(UUID senderId, UUID channelId, String content) {
        userService.validateUserExists(senderId);
        channelService.validateChannelExists(channelId);

        if(!channelService.findChannelById(channelId).getMembers().contains(senderId)){
            throw new IllegalStateException("유저가 채널에 속해있지 않음.");
        }
        Message message = new Message(senderId, channelId, content);
        channelService.addMessage(channelId, message.getId());
        messages.put(message.getId(), message);

        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        validateMessage(messageId);
        return messages.get(messageId);
    }

    @Override
    public List<Message> findMessagesByUserAndChannel(UUID senderId, UUID channelId) {
        return messages.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findChannelMessages(UUID channelId) {
        return messages.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findUserMessages(UUID senderId) {
        return messages.values().stream()
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
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
        channelService.removeMessage(message.getChannelId(), messageId);
        messages.remove(messageId);
    }

    @Override
    public void validateMessage(UUID messageId) {
        if (!messages.containsKey(messageId)) {
            throw new NoSuchElementException("존재하지 않는 메세지입니다.");
        }
    }
}
