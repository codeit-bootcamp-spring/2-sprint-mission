package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.*;

import java.util.*;

public class JCFMessageService implements MessageService {
    private volatile static JCFMessageService intance = null;
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (intance == null) {
            synchronized (JCFMessageService.class) {
                if (intance == null)
                    intance = new JCFMessageService(userService, channelService);
            }
        }
        return intance;
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        // User가 존재하는지 검증
        if(userService.getUserById(userId).isEmpty()) return null;

        // Channel이 존재하는지 검증
        if(channelService.getChannelById(channelId).isEmpty()) return null;

        Message message = new Message(userId, channelId, content);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> getAllMessages() {
        return data.values().stream().toList();
    }

    @Override
    public void updateMessageContent(UUID id, String content) {
        if(data.containsKey(id)) {
            data.get(id).updateContent(content);
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
