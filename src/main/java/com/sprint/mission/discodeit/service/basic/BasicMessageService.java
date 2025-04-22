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
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    //
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        UUID channelId = messageCreateRequest.getChannelId();
        UUID authorId = messageCreateRequest.getAuthorId();

        Channel channel = channelRepository.findById(channelId).orElseThrow(NoSuchElementException::new);
        User author = userRepository.findById(authorId).orElseThrow(NoSuchElementException::new);

        List<BinaryContent> attachments = binaryContentCreateRequests.stream()
                .map(attachmentRequest -> {
                    String fileName = attachmentRequest.getFileName();
                    String contentType = attachmentRequest.getContentType();
                    byte[] bytes = attachmentRequest.getBytes();

                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(createdBinaryContent.getId(), bytes);
                    return createdBinaryContent;
                })
                .toList();

        String content = messageCreateRequest.getContent();
        Message message = Message.builder()
                .content(content)
                .channel(channel)
                .author(author)
                .attachments(attachments)
                .build();
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public Slice<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
        Slice<Message> messages = messageRepository.findAllByChannelId(channelId, pageable);
        return messages.map(messageMapper::toDto);
    }

    @Override
    @Transactional
    public Message update(UUID messageId, MessageUpdateRequest request) {
        String newContent = request.getNewContent();
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.update(newContent);
        return message;
    }

    @Override
    @Transactional
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.getAttachments()
                .forEach(binaryContentRepository::delete);
        messageRepository.deleteById(messageId);
    }
}
