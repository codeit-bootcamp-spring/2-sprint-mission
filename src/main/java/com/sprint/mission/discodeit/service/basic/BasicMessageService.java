package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.message.Message;
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
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateRequest requestDto) {
        validateChannelExistence(requestDto.channelId());
        validateUserExistence(requestDto.authorId());

        List<UUID> attachmentIds = requestDto.attachmentIds() != null ? requestDto.attachmentIds() : List.of();

        Message message = new Message(requestDto.content(), requestDto.channelId(), requestDto.authorId(), attachmentIds);

        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        validateChannelExistence(channelId);
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public Message update(MessageUpdateRequest requestDto) {
        Message message = getMessage(requestDto.id());
        message.update(requestDto.content());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = getMessage(messageId);

        if (message.getAttachmentIds().isEmpty()) {
            messageRepository.deleteById(messageId);
            return;
        }

        message.getAttachmentIds()
                .forEach(attachmentId -> {
                    if (binaryContentRepository.existsById(attachmentId)) {
                        binaryContentRepository.deleteById(attachmentId);
                    }
                });
        messageRepository.deleteById(messageId);
    }

    private Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메세지 없음"));
    }

    private void validateUserExistence(UUID authorId) {
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("해당 유저 없음");
        }
    }

    private void validateChannelExistence(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("해당 채널 없음");
        }
    }
}
