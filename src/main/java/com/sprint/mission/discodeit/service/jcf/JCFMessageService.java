package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        if (userService.getUser(userId) == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        if (channelService.getChannel(channelId) == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }

        Message message = new Message(userId, channelId, content);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        Message message = data.get(id);
        if (message != null) {
            message.updateContent(newContent); //
        }
    }
}
