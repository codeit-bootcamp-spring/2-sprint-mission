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
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    //
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;
    private final PageResponseMapper pageResponseMapper;

    @Transactional
    @Override
    public MessageDto create(
        MessageCreateRequest messageCreateRequest,
        List<BinaryContentCreateRequest> binaryContentCreateRequests
    ) {
        log.info("Message 생성 시작");
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(
                () -> new ChannelNotFoundException(channelId));
        User author = userRepository.findById(authorId)
            .orElseThrow(
                () -> new UserNotFoundException(authorId)
            );

        List<BinaryContent> attachments = binaryContentCreateRequests.stream()
            .map(attachmentRequest -> {
                log.info("binaryContent 생성 시작");
                String fileName = attachmentRequest.fileName();
                String contentType = attachmentRequest.contentType();
                byte[] bytes = attachmentRequest.bytes();

                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                    contentType);
                binaryContentRepository.save(binaryContent);
                binaryContentStorage.put(binaryContent.getId(), bytes);
                log.info("BinaryContent 생성 완료");
                return binaryContent;
            })
            .toList();

        String content = messageCreateRequest.content();
        Message message = new Message(
            content,
            channel,
            author,
            attachments
        );

        messageRepository.save(message);
        log.info("Message 생성 완료");
        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageDto find(UUID messageId) {
        return messageRepository.findById(messageId)
            .map(messageMapper::toDto)
            .orElseThrow(
                () -> new DiscodeitException(ErrorCode.INFO_NOT_FOUND,
                    Map.of("Meesage is not exist", messageId)));
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
        Pageable pageable) {
        Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
                Optional.ofNullable(createAt).orElse(Instant.now()),
                pageable)
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
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        log.info("Message 업데이트 시작");
        String newContent = request.newContent();
        Message message = messageRepository.findById(messageId)
            .orElseThrow(
                () -> new DiscodeitException(ErrorCode.INFO_NOT_FOUND,
                    Map.of("Message is not exist", messageId)));
        message.update(newContent);
        log.info("Message 업데이트 완료");
        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            log.warn("Message가 존재하지 않습니다.");
            throw new DiscodeitException(ErrorCode.INFO_NOT_FOUND,
                Map.of("Meesage is not exist", messageId));
        }
        log.info("Message 삭제 시작");
        messageRepository.deleteById(messageId);
        log.info("Message 삭제 완료");
    }
}
