package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public JCFMessageService(MessageRepository messageRepository, ChannelService channelService, UserService userService) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        if (!channelService.exists(channelId)){
            throw new NoSuchElementException("Channel not found");
        }

        if(!userService.exists(authorId)){
            throw new NoSuchElementException("Author not found");
        }

        Message message = new Message(content, channelId, authorId);
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId);
        message.update(newContent);

        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
