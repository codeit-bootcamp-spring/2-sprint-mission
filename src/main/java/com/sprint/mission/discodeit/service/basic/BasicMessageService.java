package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.groups.ChannelType;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    @Override
    public void create(MessageCreateDto messageCreateDto) {
        channelService.findById(messageCreateDto.channelId());

        if (messageRepository.findById(messageCreateDto.id()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 메세지 입니다: " + messageCreateDto.id());
        }

        User user = userRepository.findById(messageCreateDto.authorId()).orElseThrow(IllegalArgumentException::new);

        messageRepository.save(new Message(messageCreateDto.id(), Instant.now(), messageCreateDto.content(), messageCreateDto.channelId(), messageCreateDto.authorId(), user.getProfileId()));
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 메시지를 찾을 수 없습니다." + id));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return messageRepository
                .findByChannelId(channelId)
                .orElse(Collections.emptyList());
    }

    @Override
    public void update(MessageUpdateDto messageUpdateDto) {
        Message message = this.findById(messageUpdateDto.id());
        message.setContent(messageUpdateDto.content());
        messageRepository.update(message);
    }

    @Override
    public void delete(UUID id) {
        this.findById(id);
        messageRepository.delete(id);
    }
}


