package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private volatile static JCFMessageService instance = null;
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (JCFMessageService.class) {
                if (instance == null) {
                    instance = new JCFMessageService(userService, channelService);
                }
            }
        }

        return instance;
    }

    @Override
    public Message create(UUID authorId, UUID channelId, String content) {
        try {
            userService.findById(authorId);
            channelService.findById(channelId);
        } catch (NoSuchElementException e) {
            throw e;
        }
        Message message = new Message(authorId, channelId, content);
        data.put(message.getId(), message);

        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId))
                .orElseThrow(() -> new NoSuchElementException(messageId + " 메시지를 찾을 수 없습니다."));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> findByAuthorId(UUID authorId) {
        return data.values().stream()
                .filter(message -> message.getAuthorId().equals(authorId))
                .toList();
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = Optional.ofNullable(data.get(messageId))
                .orElseThrow(() -> new NoSuchElementException(messageId + " 메시지를 찾을 수 없습니다."));
        message.update(newContent);

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (!data.containsKey(messageId)) {
            throw new NoSuchElementException(messageId + " 메시지를 찾을 수 없습니다.");
        }
        data.remove(messageId);
    }
}
