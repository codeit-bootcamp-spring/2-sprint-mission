package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateMessageDto;
import com.sprint.mission.discodeit.dto.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(CreateMessageDto messageDto) {
        if (!channelRepository.existsByKey(messageDto.channelKey())) {
            throw new NoSuchElementException("[Error] 채널이 존재하지 않습니다. " + messageDto.channelKey());
        }
        if (!userRepository.existsByKey(messageDto.userKey())) {
            throw new NoSuchElementException("[Error] 유저가 존재하지 않습니다. " + messageDto.userKey());
        }
        Message message = new Message(messageDto.content(), messageDto.userKey(), messageDto.channelKey(), messageDto.attachmentKeys());
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message read(UUID messageKey) {
        Message message = messageRepository.findByKey(messageKey);
        if (message == null) {
            throw new IllegalStateException("[Error] 읽을 메시지가 없습니다.");
        }
        return message;
    }

    @Override
    public List<Message> readAllByChannelKey(UUID channelKey) {
        List<Message> messages = messageRepository.findAllByChannelKey(channelKey);
        if (messages.isEmpty()) {
            throw new IllegalStateException("[Error] 읽을 메시지가 없습니다.");
        }
        return messages;
    }

    @Override
    public UpdateMessageDto update(UpdateMessageDto dto) {
        Message message = messageRepository.findByKey(dto.messageKey());
        if (message == null) {
            throw new IllegalArgumentException("[Error] 해당 메시지가 존재하지 않습니다");
        }
        if (!dto.content().isEmpty()) {
            message.updateContent(dto.content());
        }
        return new UpdateMessageDto(message.getUuid(), message.getContent());
    }

    @Override
    public void delete(UUID messageKey) {
        Message message = messageRepository.findByKey(messageKey);
        if (message == null) {
            throw new IllegalArgumentException("[Error] 해당 메시지가 존재하지 않습니다");
        }
        List<UUID> attachmentKeys = message.getAttachmentKeys();
        for (UUID attachmentKey : attachmentKeys) {
            binaryContentRepository.delete(attachmentKey);
        }

        messageRepository.delete(messageKey);
    }
}
