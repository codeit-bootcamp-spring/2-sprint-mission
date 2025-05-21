package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;

    @Transactional
    @Override
    public MessageDto create(MessageCreateRequest messageCreateRequest,
                             List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        log.info("Creating message: {}", messageCreateRequest);
        User user = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> {
                    log.warn("User not found: userId={}", messageCreateRequest.authorId());
                    return new UserNotFoundException(messageCreateRequest.authorId());
                });

        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> {
                    log.warn("Channel not found: channelId={}", messageCreateRequest.channelId());
                    return new ChannelNotFoundException(messageCreateRequest.channelId());
                });

        List<BinaryContent> attachments = new ArrayList<>();

        for (BinaryContentCreateRequest dto : binaryContentCreateRequests) {
            BinaryContent binaryContent = new BinaryContent(dto.fileName(), (long) dto.bytes().length,
                    dto.contentType());
            binaryContentRepository.save(binaryContent);
            binaryContentStorage.put(binaryContent.getId(), dto.bytes());
            attachments.add(binaryContent);
            log.debug("Attachment created: {}", binaryContent);
        }

        Message newMessage = new Message(user, channel,
                messageCreateRequest.content(), attachments);
        messageRepository.save(newMessage);
        log.info("Message created successfully: messageId={}", newMessage.getId());

        return messageMapper.toDto(newMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageDto findById(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createdAt, Pageable pageable) {
        Slice<MessageDto> messageDtoSlice = messageRepository.findAllByChannelIdWithAuthor(channelId,
                        Optional.ofNullable(createdAt).orElse(Instant.now()), pageable)
                .map(messageMapper::toDto);

        Instant nextCursor = null;
        if (!messageDtoSlice.getContent().isEmpty()) {
            nextCursor = messageDtoSlice.getContent().get(messageDtoSlice.getContent().size() - 1).createdAt();
        }

        return pageResponseMapper.fromSlice(messageDtoSlice, nextCursor);
    }

    @Transactional
    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        log.info("Updating message: messageId={}, updateDto={}", messageId, messageUpdateRequest);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.warn("Message not found: messageId={}", messageId);
                    return new MessageNotFoundException(messageId);
                });
        message.update(messageUpdateRequest.newContent());
        log.info("Message updated successfully: messageId={}", messageId);

        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void delete(UUID messageId) {
        log.info("Deleting message: messageId={}", messageId);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        messageRepository.delete(message);
        log.info("Message deleted successfully: messageId={}", messageId);
    }
}
