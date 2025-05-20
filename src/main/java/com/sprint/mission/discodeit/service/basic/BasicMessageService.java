package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
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
    public MessageDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos) {
        log.info("Creating message: {}", messageCreateDto);
        User user = userRepository.findById(messageCreateDto.authorId())
                .orElseThrow(() -> {
                    log.warn("User not found: userId={}", messageCreateDto.authorId());
                    return new UserNotFoundException(messageCreateDto.authorId());
                });

        Channel channel = channelRepository.findById(messageCreateDto.channelId())
                .orElseThrow(() -> {
                    log.warn("Channel not found: channelId={}", messageCreateDto.channelId());
                    return new ChannelNotFoundException(messageCreateDto.channelId());
                });

        List<BinaryContent> attachments = new ArrayList<>();

        for (BinaryContentCreateDto dto : binaryContentCreateDtos) {
            BinaryContent binaryContent = new BinaryContent(dto.fileName(), (long) dto.bytes().length,
                    dto.contentType());
            binaryContentRepository.save(binaryContent);
            binaryContentStorage.put(binaryContent.getId(), dto.bytes());
            attachments.add(binaryContent);
            log.debug("Attachment created: {}", binaryContent);
        }

        Message newMessage = new Message(user, channel,
                messageCreateDto.content(), attachments);
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
    public MessageDto update(UUID messageId, MessageUpdateDto messageUpdateDto) {
        log.info("Updating message: messageId={}, updateDto={}", messageId, messageUpdateDto);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.warn("Message not found: messageId={}", messageId);
                    return new MessageNotFoundException(messageId);
                });
        message.update(messageUpdateDto.newContent());
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
