package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateRequest request) {
        if (!channelRepository.existsByKey(request.channelKey())) {
            throw new NoSuchElementException("[Error] 채널이 존재하지 않습니다. " + request.channelKey());
        }
        if (!userRepository.existsByKey(request.authorKey())) {
            throw new NoSuchElementException("[Error] 유저가 존재하지 않습니다. " + request.authorKey());
        }
        Message message = new Message(request.content(), request.authorKey(), request.channelKey(), request.attachmentKeys());
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
    public Message update(MessageUpdateRequest request) {
        Message message = messageRepository.findByKey(request.messageKey());
        if (message == null) {
            throw new IllegalArgumentException("[Error] 해당 메시지가 존재하지 않습니다");
        }
        if (!request.newContent().isEmpty()) {
            message.updateContent(request.newContent());
        }
        return message;
    }

    @Override
    public void delete(UUID messageKey) {
        Message message = messageRepository.findByKey(messageKey);
        if (message == null) {
            throw new IllegalArgumentException("[Error] 해당 메시지가 존재하지 않습니다");
        }

        message.getAttachmentKeys().forEach(binaryContentRepository::delete);

        messageRepository.delete(messageKey);
    }
}
