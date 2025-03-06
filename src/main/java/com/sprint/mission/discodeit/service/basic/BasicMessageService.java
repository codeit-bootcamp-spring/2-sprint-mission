package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private volatile static BasicMessageService instance = null;
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    public BasicMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    public static BasicMessageService getInstance(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        if (instance == null) {
            synchronized (BasicMessageService.class) {
                if(instance == null) {
                    instance = new BasicMessageService(userService, channelService, messageRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public Message saveMessage(Channel channel, User user, String text) {
        if(channelService.findById(channel.getId()) == null) {
            throw new NoSuchElementException("메시지를 등록할 채널이 없습니다.");
        }
        if(userService.findById(user.getId()) == null) {
            throw new NoSuchElementException("메시지를 등록할 유저가 없습니다.");
        }

        Message message = new Message(channel.getId(), user.getId(), text);
        messageRepository.save(message);
        return message;
    }

    @Override
    public List<Message> findAll() {
        if (messageRepository.findAll().isEmpty()) {
            throw new NoSuchElementException("등록된 메시지가 없습니다.");
        }
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> findById(UUID id) {
        try{
            return Optional.ofNullable(messageRepository.findById(id));
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("등록된 메시지가 없습니다. : " + id);
        }
    }

    @Override
    public void update(UUID id, String text) {
        try{
            Message message = messageRepository.findById(id);
            message.setText(text);
            messageRepository.save(message);
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("업데이트 할 메시지가 없습니다. ");
        }catch(NullPointerException e){
            throw new NullPointerException("수정할 내용은 null일 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
