package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messages = new HashMap<>();
    private final JCFUserService userService;
    private final JCFChannelService channelService;

    public JCFMessageService(JCFUserService userService, JCFChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
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

        if (!channel.isMember(sender)) {
            System.out.println("유저가 채널에 등록되어 있지 않습니다.");
            return;
        }

        messages.put(message.getId(), message);
    }

    @Override
    public Message find(UUID id) {
        return messages.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
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

        messages.put(message.getId(), message);
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
