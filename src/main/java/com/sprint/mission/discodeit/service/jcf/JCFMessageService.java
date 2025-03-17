package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private static final JCFMessageService instance = new JCFMessageService(JCFUserService.getInstance(), JCFChannelService.getInstance());
    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance() {
        return instance;
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String text) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("채널 ID는 null일 수 없습니다.");
        }
        if (!userService.getUserById(userId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId);
        }
        if (!channelService.getChannelById(channelId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId);
        }
        Message message = new Message(userId, channelId, text);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> getMessageById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId));
    }

    @Override
    public List<Message> getAllMessagesByChannel(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(UUID messageId, String newText) {
        Message message = data.get(messageId);
        if (message != null) {
            Instant currentTime = Instant.now();
            message.update(newText, currentTime);
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        data.remove(messageId);
    }
}
