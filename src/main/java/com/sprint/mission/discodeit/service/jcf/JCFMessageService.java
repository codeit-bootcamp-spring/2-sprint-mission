package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data = new HashMap<>();
    private final AtomicInteger messageNum = new AtomicInteger(1);
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public UUID create(String content, UUID userUuid, UUID channelUuid) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("[Error] 내용을 입력해주세요");
        }
        int messageId = messageNum.getAndIncrement();
        Message text = new Message(messageId, content, userService.getUserName(userUuid), channelService.getChannelName(channelUuid));
        data.put(text.getUuid(), text);
        return text.getUuid();
    }

    @Override
    public Message read(UUID channelUuid) {
        Message lastMessage = getLastMessage(channelUuid);
        if (lastMessage == null) {
            throw new IllegalStateException("[Error] 읽을 메시지가 없습니다.");
        }
        return lastMessage;
    }

    @Override
    public List<Message> readAll(UUID channelUuid) {
        List<Message> allMessage = getAllMessage(channelUuid);
        if (allMessage.isEmpty()) {
            throw new IllegalStateException("[Error] 읽을 메시지가 없습니다.");
        }
        return getAllMessage(channelUuid);
    }

    @Override
    public UUID update(int messageId, String content) {
        UUID messageKey = convertToKey(messageId);
        Message message = data.get(messageKey);
        if (message == null) {
            throw new IllegalArgumentException("[Error] 해당 메시지가 존재하지 않습니다");
        }
        if (!content.isEmpty()) {
            message.updateContent(content);
        }
        return messageKey;
    }

    @Override
    public void delete(int messageId) {
        UUID messageUuid = convertToKey(messageId);
        Message message = data.get(messageUuid);
        if (message == null) {
            throw new IllegalArgumentException("[Error] 삭제할 메시지가 존재하지 않습니다");
        }
        data.remove(messageUuid);
    }

    private Message getLastMessage(UUID channelUuid) {
        return data.values().stream()
                .filter(m -> m.getChannelName().equals(channelService.getChannelName(channelUuid)))
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElse(null);
    }

    private List<Message> getAllMessage(UUID channelUuid) {
        return data.values().stream()
                .filter(m -> m.getChannelName().equals(channelService.getChannelName(channelUuid)))
                .toList();
    }

    private UUID convertToKey(int messageId) {
        return data.values().stream()
                .filter(m -> m.getMessageId() == messageId)
                .map(Message::getUuid)
                .findFirst()
                .orElse(null);
    }
}
