package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentStorage;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(BasicMessageService.class);
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageMapper;
    private final BinaryContentService binaryContentService;

    @Transactional
    @Override
    public MessageDto create(MessageCreateRequest messageCreateRequest,
        List<MultipartFile> attachments) {
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        Channel channel = findChannelOrThrow(channelId);
        User author = findUserOrThrow(authorId);
        log.info("채널 및 작성자 확인 완료");

        List<BinaryContent> savedAttachmentEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(attachments)) {
            for (int i = 0; i < attachments.size(); i++) {
                MultipartFile file = attachments.get(i);
                if (file == null || file.isEmpty()) {
                    log.warn("첨부파일이 비어 있음");
                    continue;
                }

                String fileName = file.getOriginalFilename();
                try {
                    BinaryContentDto attachmentDto = binaryContentService.create(file);

                    BinaryContent savedAttachment = binaryContentRepository.findById(
                            attachmentDto.id())
                        .orElseThrow(() -> {
                            log.error("첨부파일 메타데이터를 찾을 수 없음");
                            return new MessageException(ErrorCode.FILE_PROCESSING_ERROR,
                                Map.of("fileName", fileName, "detail",
                                    "저장된 첨부파일 메타데이터를 찾을 수 없습니다."));
                        });
                    savedAttachmentEntities.add(savedAttachment);
                    log.info("첨부파일 저장 완료");
                } catch (Exception e) {
                    log.error("첨부파일 처리 오류", e);
                    throw new MessageException(ErrorCode.FILE_PROCESSING_ERROR,
                        Map.of("fileName", fileName, "originalError", e.getMessage()));
                }
            }
            log.info("첨부파일 처리 완료");
        } else {
            log.info("첨부파일 없음");
        }

        Message message = new Message(
            messageCreateRequest.content(),
            channel,
            author,
            savedAttachmentEntities
        );

        Message savedMessage = messageRepository.save(message);
        log.info("메시지 저장 완료");
        return messageMapper.toDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageException(ErrorCode.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId.toString())));
        log.info("메시지 조회 완료");
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, String cursor, int size) {
        if (!channelRepository.existsById(channelId)) {
            log.warn("채널 없음");
            throw new ChannelException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channelId", channelId.toString()));
        }

        List<Message> messages;
        if (cursor != null) {
            Instant cursorTime = Instant.parse(cursor);
            messages = messageRepository.findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(
                channelId, cursorTime, PageRequest.of(0, size + 1));
        } else {
            messages = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId,
                PageRequest.of(0, size + 1));
        }

        boolean hasNext = messages.size() > size;
        List<Message> pageContent = hasNext ? messages.subList(0, size) : messages;
        log.info("메시지 조회 완료");
        return pageMapper.messageListToPageResponse(pageContent, size, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public Instant lastMessageTime(UUID channelId) {
        return messageRepository
            .findTopByChannelIdOrderByCreatedAtDesc(channelId)
            .map(Message::getCreatedAt)
            .orElse(Instant.now());
    }

    @Override
    @Transactional
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageException(ErrorCode.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId.toString())));

        String newContent = request.newContent();
        message.update(newContent);
        Message updatedMessage = messageRepository.save(message);
        log.info("메시지 업데이트 완료");
        return messageMapper.toDto(updatedMessage);
    }

    @Override
    @Transactional
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            log.warn("메시지 없음");
            throw new MessageException(ErrorCode.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId.toString()));
        }
        messageRepository.deleteById(messageId);
        log.info("메시지 삭제 완료");
    }

    private Channel findChannelOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new ChannelException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channelId", channelId.toString())));
    }

    private User findUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }
}