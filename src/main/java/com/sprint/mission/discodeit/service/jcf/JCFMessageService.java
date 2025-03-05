package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private volatile static JCFMessageService instance = null;
    private final UserService userService;
    private final ChannelService channelService;
    private final Map<UUID, Message> messageRepository;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.messageRepository = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (JCFMessageService.class) {
                if(instance == null) {
                    instance = new JCFMessageService(userService, channelService);
                }
            }
        }
        return instance;
    }

    @Override
    public Message saveMessage(Channel channel, User user, String text) {
        if(channelService.findById(channel.getId()).isEmpty()){
            throw new NoSuchElementException("not found channel");
        }
        if(userService.findById(user.getId()).isEmpty()){
            throw new NoSuchElementException("not found user");
        }

        Message message = new Message(channel.getId(), user.getId(), text);
        messageRepository.put(message.getId(), message);
        return message;
    }

    @Override
    public List<Message> findAll() {
        if(messageRepository.isEmpty()){
            throw new NoSuchElementException("메시지가 없습니다.");
        }

        return messageRepository.values().stream().toList();
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageRepository.get(id));
    }

    @Override
    public void update(UUID id, String text) {
        if(!messageRepository.containsKey(id)){
            throw new NoSuchElementException("메시지가 없습니다.");
        }
        if(text == null){
            throw new IllegalArgumentException("수정할 내용은 null일 수 없습니다.");
        }

        messageRepository.get(id).setText(text);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.remove(id);
    }
}
