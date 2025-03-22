package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateRequestDto requestDto) {
        if (!channelRepository.existsById(requestDto.getChannelId())) {
            throw new NoSuchElementException("Channel not found with id " + requestDto.getChannelId());
        }
        if (!userRepository.existsById(requestDto.getAuthorId())) {
            throw new NoSuchElementException("Author not found with id " + requestDto.getAuthorId());
        }

        Message message = new Message(requestDto.getContent(), requestDto.getChannelId(), requestDto.getAuthorId());

        if (requestDto.getAttachmentDataList() != null) {
            for (byte[] data : requestDto.getAttachmentDataList()) {
                BinaryContent binaryContent = new BinaryContent(data, null, message.getId());
                binaryContentRepository.save(binaryContent);
                message.addAttachment(binaryContent.getId());
            }
        }
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent,List<UUID> newAttachmentIds) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.update(newContent, newAttachmentIds);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        messageRepository.deleteByMessageId(messageId);
    }
}
