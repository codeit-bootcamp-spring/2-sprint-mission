package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request) {
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("Channel not found with id " + request.channelId());
        }
        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("Author not found with id " + request.authorId());
        }

        Message message = new Message(request.content(), request.channelId(), request.authorId());
        messageRepository.save(message);

        List<UUID> attachmentIds = new ArrayList<>();
        if (request.attachments() != null) {
            for (byte[] file : request.attachments()) {
                BinaryContent binaryContent = new BinaryContent(null, message.getId());
                binaryContentRepository.save(binaryContent);
                attachmentIds.add(binaryContent.getId());
            }
        }

        return new MessageResponse(message.getId(), message.getCreatedAt(), message.getContent(), message.getChannelId(), message.getAuthorId(), attachmentIds);
    }

    @Override
    public Optional<MessageResponse> find(UUID messageId) {
        return messageRepository.findById(messageId).map(message -> {
            List<UUID> attachmentIds = binaryContentRepository.findByMessageId(message.getId()).stream()
                    .map(BinaryContent::getId)
                    .collect(Collectors.toList());
            return new MessageResponse(message.getId(), message.getCreatedAt(), message.getContent(), message.getChannelId(), message.getAuthorId(), attachmentIds);
        });
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId).stream()
                .map(message -> {
                    List<UUID> attachmentIds = binaryContentRepository.findByMessageId(message.getId()).stream()
                            .map(BinaryContent::getId)
                            .collect(Collectors.toList());
                    return new MessageResponse(message.getId(), message.getCreatedAt(), message.getContent(), message.getChannelId(), message.getAuthorId(), attachmentIds);
                }).collect(Collectors.toList());
    }

    @Override
    public void update(MessageUpdateRequest request) {
        Message message = messageRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("Message with id " + request.id() + " not found"));

        message.update(request.newContent());
        messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }

        binaryContentRepository.findByMessageId(messageId).forEach(file -> binaryContentRepository.deleteById(file.getId()));
        messageRepository.deleteById(messageId);
    }
}
