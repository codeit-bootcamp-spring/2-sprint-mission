package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public BasicMessageService(MessageRepository messageRepository, ChannelService channelService, UserService userService) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID userId) {
        try {
            channelService.findById(channelId);
            userService.findById(userId);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("유효하지 않은 채널 ID 또는 유저 ID입니다.", e);
        }
        Message message = new Message(content, channelId, userId);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId));
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId));
        message.update(newContent);
        return messageRepository.update(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.delete(messageId)) {
            throw new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId);
        }
    }
}
