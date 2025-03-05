package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class FileMessageService implements MessageService {
    private MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(MessageRepository messageRepository, ChannelService channelService, UserService userService) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try{
            channelService.find(channelId);             //채널 존재 확인하는 비즈니스 로직. JCF구현체와 동일
            userService.find(authorId);                 //사용자 존재 확인하는 비즈니스 로직. JCF구현체와 동일
        } catch (NoSuchElementException e){
            throw e;
        }

        Message message = new Message(content, channelId, authorId);    //메세지 생성하는 비즈니스 로직. JCF 동일.
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        return message;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId);
        if (message == null) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        message.update(newContent);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.exists(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        messageRepository.delete(messageId);
    }

}
