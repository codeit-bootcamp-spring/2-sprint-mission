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
    private static BasicMessageService instance;
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    private BasicMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageRepository = messageRepository;
    }

    public synchronized static BasicMessageService getInstance(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        if (instance == null) {
            instance = new BasicMessageService(userService, channelService, messageRepository);
        }
        return instance;
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
        System.out.println("[" + message +"] 생성 완료 " + message.getId());
    }

    @Override
    public Message find(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID가 NULL입니다.");
        }

        if (messageRepository.find(id) == null) {
            throw new RuntimeException("메시지가 존재하지 않습니다.");
        }
        System.out.println("선택한 메시지를 조회합니다.");
        return messageRepository.find(id);
    }

    @Override
    public List<Message> findAll() {
        if (messageRepository.findAll().isEmpty()) {
            System.out.println("등록된 메시지가 없습니다.");
        }
        System.out.println("모든 메시지를 조회합니다.");
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
        System.out.println("[" + message +"] 수정 완료 " + message.getId());
        messageRepository.update(message);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID가 NULL입니다.");
        }

        if (messageRepository.find(id) == null) {
            throw new RuntimeException("메시지가 존재하지 않습니다.");
        }
        messageRepository.delete(id);
        System.out.println("메시지 삭제를 완료하였습니다.");
    }
}
