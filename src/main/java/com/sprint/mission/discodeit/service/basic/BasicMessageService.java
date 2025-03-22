package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.messagedto.MessageCreationDto;
import com.sprint.mission.discodeit.application.messagedto.MessageDto;
import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public MessageDto create(MessageCreationDto messageCreationDto, List<UUID> attachmentsIds) {
        Message message = messageRepository.save(new Message(messageCreationDto.context(), messageCreationDto.chanelId(), messageCreationDto.userId(), attachmentsIds));

        return MessageDto.fromEntity(message, UserDto.fromEntity(findMessageUser(message)));
    }

    @Override
    public MessageDto findById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        return MessageDto.fromEntity(message, UserDto.fromEntity(findMessageUser(message)));
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAll()
                .stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(message -> MessageDto.fromEntity(message, UserDto.fromEntity(findMessageUser(message))))
                .toList();
    }

    @Override
    public MessageDto updateContext(UUID id, String context) {
        Message message = messageRepository.updateContext(id, context);
        return MessageDto.fromEntity(message, UserDto.fromEntity(findMessageUser(message)));
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
