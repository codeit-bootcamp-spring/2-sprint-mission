package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.async.BinaryContentAsyncService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final MessageMapper messageMapper;

    private final BinaryContentStorage binaryContentStorage;

    private final PageResponseMapper pageResponseMapper;

    private final BinaryContentAsyncService binaryContentAsyncService;

    @Transactional
    @Override
    public MessageDto create(
        MessageCreateRequest messageCreateRequest,
        List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        log.info("Creating message");

        String content = messageCreateRequest.content();

        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> {
                log.warn("Channel not found : id = {}", channelId);
                return ChannelNotFoundException.withId(channelId);
            });
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> {
                log.warn("Author not found : id = {}", authorId);
                return UserNotFoundException.withId(authorId);
            });

        List<BinaryContent> attachments = binaryContentCreateRequests.stream()
            .map(request -> {
                BinaryContent binaryContent = new BinaryContent(
                    request.fileName(),
                    (long) request.bytes().length,
                    request.contentType()
                );
                log.debug("Attachment saved: id = {}, filename = {}", binaryContent.getId(),
                    binaryContent.getFileName());
                binaryContent.setUploadStatus(BinaryContentUploadStatus.WAITING);
                binaryContentRepository.save(binaryContent);
                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            binaryContentAsyncService.uploadFile(binaryContent.getId(), request.bytes());
                        }
                    }
                );
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

        log.info("Message created successfully: id = {}", message.getId());

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
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createdAt,
        Pageable pageable) {

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

    @PreAuthorize("hasPermission(#messageId,'Message','update')")
    @Transactional
    @Override
    public MessageDto updateMessage(UUID messageId, MessageUpdateRequest request) {
        log.info("Updating message : id = {}", messageId);

        String newContent = request.newContent();
        Message message = getMessage(messageId);
        message.updateContent(newContent);

        log.info("Message updated successfully : id = {}", message.getId());

        return messageMapper.toDto(message);
    }

    @PreAuthorize("hasPermission(#messageId,'Message','delete')")
    @Transactional
    @Override
    public void deleteMessage(UUID messageId) {
        log.info("Deleting message : id = {}", messageId);

        if (!messageRepository.existsById(messageId)) {
            throw MessageNotFoundException.withId(messageId);
        }

        messageRepository.deleteById(messageId);

        log.info("Message deleted successfully : id = {}", messageId);
    }

    private Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
            .orElseThrow(() -> {
                log.warn("Message not found : id = {}", messageId);
                return MessageNotFoundException.withId(messageId);
            });
    }
}
