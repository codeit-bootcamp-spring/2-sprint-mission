package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentStorageException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final BinaryContentStorage binaryContentStorage;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        log.info("▶▶ [SERVICE] Creating message - channelId: {}, authorId: {}", channelId, authorId);

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Channel not found - id: {}", channelId);
                    return new ChannelNotFoundException(channelId);
                });

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Author not found - id: {}", authorId);
                    return new UserNotFoundException(authorId);
                });

        Message message = Message.builder()
                .content(messageCreateRequest.content())
                .channel(channel)
                .author(author)
                .build();

        Message savedMessage = messageRepository.save(message);

        binaryContentCreateRequests.forEach(attachmentRequest -> {
            log.debug("▶▶ [SERVICE] Creating attachment - fileName: {}", attachmentRequest.fileName());
            BinaryContent binaryContent = BinaryContent.builder()
                    .fileName(attachmentRequest.fileName())
                    .size((long) attachmentRequest.bytes().length)
                    .contentType(attachmentRequest.contentType())
                    .build();

            BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

            try {
                binaryContentStorage.put(savedBinaryContent.getId(), attachmentRequest.bytes());
            } catch (Exception e) {
                log.error("◀◀ [SERVICE] Failed to save attachment to storage - fileName: {}", attachmentRequest.fileName(), e);
                throw new BinaryContentStorageException(attachmentRequest.fileName());
            }

            savedMessage.addAttachment(savedBinaryContent);
            log.debug("◀◀ [SERVICE] Attachment added to message - attachmentId: {}", savedBinaryContent.getId());
        });

        Message finalMessage = messageRepository.save(savedMessage);
        log.info("◀◀ [SERVICE] Message created successfully - id: {}", finalMessage.getId());
        return messageMapper.toDto(finalMessage);
    }

    @Override
    public MessageDto find(UUID messageId) {
        log.info("▶▶ [SERVICE] Finding message - id: {}", messageId);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Message not found - id: {}", messageId);
                    return new NoSuchElementException("Message with id " + messageId + " not found");
                });
        log.info("◀◀ [SERVICE] Message found - id: {}", messageId);
        return messageMapper.toDto(message);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        log.info("▶▶ [SERVICE] Finding all messages for channel - channelId: {}", channelId);
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        log.info("◀◀ [SERVICE] Found {} messages for channel - channelId: {}", messages.size(), channelId);
        return messageMapper.toDtoList(messages);
    }

    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        log.info("▶▶ [SERVICE] Updating message - id: {}", messageId);
        String newContent = request.newContent();
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Message not found for update - id: {}", messageId);
                    return new MessageNotFoundException(messageId);
                });
        message.setContent(newContent);
        Message updatedMessage = messageRepository.save(message);
        log.info("◀◀ [SERVICE] Message updated - id: {}", messageId);
        return messageMapper.toDto(updatedMessage);
    }

    @Override
    public void delete(UUID messageId) {
        log.info("▶▶ [SERVICE] Deleting message - id: {}", messageId);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] Message not found for deletion - id: {}", messageId);
                    return new MessageNotFoundException(messageId);
                });

        message.getAttachments().forEach(attachment -> {
            UUID attachmentId = attachment.getAttachment().getId();
            log.debug("▶▶ [SERVICE] Deleting attachment - attachmentId: {}", attachmentId);
            binaryContentRepository.deleteById(attachmentId);
            log.debug("◀◀ [SERVICE] Attachment deleted - attachmentId: {}", attachmentId);
        });

        messageRepository.deleteById(messageId);
        log.info("◀◀ [SERVICE] Message deleted - id: {}", messageId);
    }

    @Override
    public PageResponse<MessageDto> findAllByChannelIdWithPaging(UUID channelId, int page) {
        log.info("▶▶ [SERVICE] Finding messages with paging - channelId: {}, page: {}", channelId, page);
        Pageable pageable = PageRequest.of(page, 50, Sort.by("createdAt").descending());
        Slice<Message> messageSlice = messageRepository.findAllByChannelIdOrderByCreatedAtDesc(channelId, pageable);
        log.info("◀◀ [SERVICE] Found {} messages in page {} for channel - channelId: {}", messageSlice.getNumberOfElements(), page, channelId);
        return pageResponseMapper.toDto(messageSlice, messageMapper::toDto);
    }
}