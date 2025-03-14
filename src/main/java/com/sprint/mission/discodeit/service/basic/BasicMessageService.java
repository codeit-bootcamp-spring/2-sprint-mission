package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public BasicMessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MessageDto create(String context, UUID channelId, UUID userId) {
        Message message = messageRepository.save(new Message(context, channelId, userId));

        return toDto(message);
    }

    @Override
    public MessageDto findById(UUID id) {
        Message message = messageRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        return toDto(message);
    }

    @Override
    public List<MessageDto> findAll() {
        return messageRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<MessageDto> findByChannelId(UUID channelId) {
        return messageRepository.findAll()
                .stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(this::toDto)
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

    private MessageDto toDto(Message message) {
        UserDto userDto = userRepository.findById(message.getUserId())
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
                .orElseThrow(() -> new IllegalArgumentException("메세지에 등록된 아이디의 유저가 없습니다: " + message.getUserId()));

        return new MessageDto(message.getId(), message.getContext(), message.getChannelId(), userDto);
    }
}
