package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelService channelService;
    private final MessageRepository messageRepository;

    @Override
    public void create(MessageCreateRequest messageCreateDto) {
        channelService.findById(messageCreateDto.channelId());

        User user = userRepository.findById(messageCreateDto.authorId()).orElseThrow(IllegalArgumentException::new);

        messageRepository.save(new Message(Instant.now(), messageCreateDto.content(), messageCreateDto.channelId(), messageCreateDto.authorId(), user.getProfileId()));
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
    public void update(MessageUpdateRequest messageUpdateDto) {
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


