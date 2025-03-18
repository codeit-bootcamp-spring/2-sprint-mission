package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public MessageDto create(String context, UUID channelId, UUID userId) {
        Message message = messageRepository.save(new Message(context, channelId, userId));

        return MessageDto.fromEntity(message, UserDto.fromEntity(findMessageUser(message)));
    }

    @Override
    public MessageDto findById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        return MessageDto.fromEntity(message, UserDto.fromEntity(findMessageUser(message)));
    }

    @Override
    public List<MessageDto> findAll() {
        return messageRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(message -> MessageDto.fromEntity(message, UserDto.fromEntity(findMessageUser(message))))
                .toList();
    }

    @Override
    public List<MessageDto> findByChannelId(UUID channelId) {
        return messageRepository.findAll()
                .stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(message -> MessageDto.fromEntity(message, UserDto.fromEntity(findMessageUser(message))))
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        messageRepository.updateContext(id, context);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }

    private User findMessageUser(Message message) {
        return userRepository.findById(message.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("메세지에 등록된 아이디의 유저가 없습니다: " + message.getUserId()));
    }
}
