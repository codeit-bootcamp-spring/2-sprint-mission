package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;
    private final AtomicInteger messageNum = new AtomicInteger(1);

    public BasicMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, UUID userKey, UUID channelKey) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("[Error] 내용을 입력해주세요");
        }

        String username = userService.getUserName(userKey);
        String channelName = channelService.getChannelName(channelKey);
        int messageId = messageNum.getAndIncrement();

        Message message = new Message(messageId, content, userKey, channelKey, username, channelName);
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message read(UUID channelKey) {
        return messageRepository.findAllByChannelKey(channelKey).stream()
                .max((m1, m2) -> Long.compare(m1.getCreatedAt(), m2.getCreatedAt()))
                .orElseThrow(() -> new IllegalStateException("[Error] 읽을 메시지가 없습니다."));
    }

    @Override
    public List<Message> readAll(UUID channelKey) {
        List<Message> messages = messageRepository.findAllByChannelKey(channelKey);
        if (messages.isEmpty()) {
            throw new IllegalStateException("[Error] 읽을 메시지가 없습니다.");
        }
        return messages;
    }

    @Override
    public UUID update(int messageId, String content) {
        UUID messageKey = messageRepository.findKeyByMessageId(messageId);
        if (messageKey == null) {
            throw new IllegalArgumentException("[Error] 해당 메시지가 존재하지 않습니다");
        }

        Message message = messageRepository.findByKey(messageKey);
        if (!content.isEmpty()) {
            message.updateContent(content);
        }
        return messageKey;
    }

    @Override
    public void delete(int messageId) {
        UUID messageKey = messageRepository.findKeyByMessageId(messageId);
        messageRepository.delete(messageKey);
    }
}
