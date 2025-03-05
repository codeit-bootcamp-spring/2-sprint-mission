package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private static volatile JCFMessageService instance;

    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if(instance == null) {
            synchronized (JCFMessageService.class) {
                if(instance == null) {
                    instance = new JCFMessageService(userService, channelService);
                }
            }
        }
        return instance;
    }

    @Override
    public void create(Message message) {
        if(message == null) {
            throw new IllegalArgumentException("message 객체가 null 입니다.");
        }

        try {
            userService.findById(message.getUserId());
            channelService.findById(message.getChannelId());
        }catch (NoSuchElementException e) {
            throw e;
        }

        data.put(message.getId(), message);
    }

    @Override
    public Message findById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID messageId) {
        Message message = findById(messageId);
        data.remove(message.getId());
    }

    @Override
    public void update(UUID messageId, String content) {
        Message message = findById(messageId);
        message.update(content, System.currentTimeMillis());
    }
}
