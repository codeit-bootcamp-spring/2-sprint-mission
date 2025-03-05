package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class BasicMessageService implements MessageService {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    public BasicMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageRepository = messageRepository;
    }

    @Override
    public void create(Message message) {
        User sender = message.getSender();
        Channel channel = message.getChannel();

        if (userService.find(sender.getId()) == null) {
            System.out.println("유저가 존재하지 않습니다.");
            return;
        }

        if (channelService.find(channel.getId()) == null) {
            System.out.println("채널이 존재하지 않습니다.");
            return;
        }
        messageRepository.create(message);
    }

    @Override
    public Message find(UUID id) {
        return messageRepository.find(id);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public void update(Message message) {
        User sender = message.getSender();
        Channel channel = message.getChannel();

        if (userService.find(sender.getId()) == null) {
            System.out.println("유저가 존재하지 않습니다.");
            return;
        }

        if (channelService.find(channel.getId()) == null) {
            System.out.println("채널이 존재하지 않습니다.");
            return;
        }
        messageRepository.update(message);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
