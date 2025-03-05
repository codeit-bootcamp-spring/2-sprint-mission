package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageList;
    private final ChannelService channelService;
    private final UserService userService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.messageList = new HashMap<>();
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID userId) {
        try {
            channelService.findById(channelId);
            userService.findById(userId);
        } catch (NoSuchElementException e) {
            throw e;
        }
        Message message = new Message(content, channelId, userId);
        this.messageList.put(message.getId(), message);

        return message;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageList.values());
    }

    @Override
    public Message findById(UUID messageId) {
        Message message = this.messageList.get(messageId);
        return Optional.ofNullable(message)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId));
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = this.messageList.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId));
        message.update(newContent);

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        Message removeMessage = this.messageList.remove(messageId);
        if (removeMessage == null) {
            throw new NoSuchElementException("해당 메시지를 찾을 수 없습니다 : " + messageId);
        }
    }
}