package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " does not exist"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author with id " + authorId + " does not exist"));

        Message message = Message.builder()
                .content(messageCreateRequest.content())
                .channel(channel)
                .author(author)
                .build();

        Message savedMessage = messageRepository.save(message);

        binaryContentCreateRequests.forEach(attachmentRequest -> {
            BinaryContent binaryContent = BinaryContent.builder()
                    .fileName(attachmentRequest.fileName())
                    .size((long) attachmentRequest.bytes().length)
                    .contentType(attachmentRequest.contentType())
                    .bytes(attachmentRequest.bytes())
                    .build();

            BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

            savedMessage.addAttachment(savedBinaryContent);
        });

        Message finalMessage = messageRepository.save(savedMessage);
        return messageMapper.toDto(finalMessage);
    }

    @Override
    public MessageDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        return messageMapper.toDto(message);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messageMapper.toDtoList(messages);
    }

    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        String newContent = request.newContent();
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.setContent(newContent);
        Message updatedMessage = messageRepository.save(message);
        return messageMapper.toDto(updatedMessage);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        message.getAttachments().forEach(attachment -> {
            binaryContentRepository.deleteById(attachment.getAttachment().getId());
        });

        messageRepository.deleteById(messageId);
    }
}
