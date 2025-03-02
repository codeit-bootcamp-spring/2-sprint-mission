package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data;

    private final ChannelService channelService;
    private final UserService userService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.data = new HashMap<>();
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID userId, UUID channelId, String content) {
        Message message = new Message(userId, channelId, content);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return data.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID messageId, UUID userId, UUID channelId, String content) {
        Message message = data.get(messageId);
        if (message != null) {
            message.update(userId, channelId, content);
        }
        return message;
    }

    @Override
    public void deleteById(UUID messageId) {
        data.remove(messageId);
    }
}
