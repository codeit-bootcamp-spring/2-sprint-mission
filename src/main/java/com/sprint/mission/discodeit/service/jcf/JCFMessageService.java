package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static volatile JCFMessageService instance;

    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    //심화 적용
    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if(instance == null) {
            synchronized (JCFMessageService.class) {
                if(instance == null) {
                    instance = new JCFMessageService(userService, channelService);
                }
            }
        }

        return instance;
    }


    @Override
    public void create(Message message) {

        if(message == null) {
            throw new IllegalArgumentException("message 객체가 null 입니다.");
        }

        if (userService.findById(message.getUserId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다: " + message.getUserId());
        }

        if (channelService.findById(message.getChannelId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + message.getChannelId());
        }

        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public void update(UUID id, String content) {
        if (data.containsKey(id)) {
            Message message = data.get(id);
            message.setContent(content);
        }
    }
}
