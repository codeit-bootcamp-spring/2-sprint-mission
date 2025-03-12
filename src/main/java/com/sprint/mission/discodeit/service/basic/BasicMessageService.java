package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private static volatile BasicMessageService instance;

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    private BasicMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    public static BasicMessageService getInstance(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (BasicMessageService.class) {
                if (instance == null) {
                    instance = new BasicMessageService(messageRepository, userService, channelService);
                }
            }
        }
        return instance;
    }

    @Override
    public void create(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message 객체가 null 입니다.");
        }

        try {
            userService.findById(message.getUserId());
            channelService.findById(message.getChannelId());
        }catch (NoSuchElementException e) {
            throw new IllegalArgumentException("연관된 user 또는 channel이 없습니다.");
        }

        messageRepository.save(message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public void delete(UUID messageId) {
        Message message = findById(messageId);
        messageRepository.delete(message.getId());
    }

    @Override
    public void update(UUID messageId, String content) {
        Message message = findById(messageId);
        messageRepository.update(message.getId(), content);
        messageRepository.save(message);
    }
}
