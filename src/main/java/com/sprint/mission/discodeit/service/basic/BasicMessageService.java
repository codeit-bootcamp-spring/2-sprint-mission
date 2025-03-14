package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private static BasicMessageService messageService;
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    private final HashMap<UUID, Message> messages = new HashMap<>();

    public BasicMessageService(UserService userService, ChannelService channelService, MessageRepository messageRepository) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageRepository = messageRepository;
    }


    @Override
    public void create(Message message, UUID channelId, UUID authorId) {
        userService.findById(authorId);
        channelService.findById(channelId);

        if (messageRepository.findById(message.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 메세지 입니다: " + message.getId());
        }

        messageRepository.save(message);
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 메시지를 찾을 수 없습니다." + id));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll().orElse(Collections.emptyList());
    }

    @Override
    public void update(UUID id, String content, UUID channelId, UUID authorId) {
        Message message = this.findById(id);
        message.setContent(content);
        messageRepository.update(message);
    }

    @Override
    public void delete(UUID id) {
        this.findById(id);
        messageRepository.delete(id);
    }
}


