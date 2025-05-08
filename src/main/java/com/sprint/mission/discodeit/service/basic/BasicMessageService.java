package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final MessageMapper messageMapper;

    private final BinaryContentStorage binaryContentStorage;

    private final PageResponseMapper pageResponseMapper;

    @Transactional
    @Override
    public MessageDto create(
            MessageCreateRequest messageCreateRequest,
            List<BinaryContentCreateRequest> binaryContentCreateRequests
    ) {
        String content = messageCreateRequest.content();

        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author with id " + authorId + " does not exist"));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " does not exist"));

        List<BinaryContent> attachments = binaryContentCreateRequests.stream()
                .map(request -> {
                    BinaryContent binaryContent = new BinaryContent(
                            request.fileName(),
                            (long) request.bytes().length,
                            request.contentType()
                    );
                    binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), request.bytes());
                    return binaryContent;
                })
                .toList();

        Message message = new Message(
                content,
                channel,
                author,
                attachments
        );
        messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageDto searchMessage(UUID messageId) {
        Message message = getMessage(messageId);
        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createdAt, Pageable pageable) {

        Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
                        Optional.ofNullable(createdAt).orElse(Instant.now()), pageable)
                .map(messageMapper::toDto);

        Instant nextCursor = null;
        if (!slice.getContent().isEmpty()) {
            nextCursor = slice.getContent().get(slice.getContent().size() - 1)
                    .createdAt();
        }
        return pageResponseMapper.fromSlice(slice, nextCursor);
    }

    @Transactional
    @Override
    public MessageDto updateMessage(UUID messageId, MessageUpdateRequest request) {
        String newContent = request.newContent();
        Message message = getMessage(messageId);
        message.updateContent(newContent);
        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void deleteMessage(UUID messageId) {
        Message message = getMessage(messageId);

        messageRepository.delete(message);
    }

    private Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }
}
