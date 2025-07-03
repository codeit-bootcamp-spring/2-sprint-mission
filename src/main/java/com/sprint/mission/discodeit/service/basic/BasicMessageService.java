package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageFileProcessingException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicMessageService implements MessageService {

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
        log.info("🔧 MessageService.create 시작");
        log.info("🔧 요청 내용: {}", messageCreateRequest);
        log.info("🔧 첨부파일 개수: {}", attachments != null ? attachments.size() : 0);
        
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();
        
        log.info("🔧 채널 조회 시작 - channelId: {}", channelId);
        Channel channel = findChannelOrThrow(channelId);
        log.info("🔧 채널 조회 성공 - 채널명: {}", channel.getName());
        
        log.info("🔧 사용자 조회 시작 - authorId: {}", authorId);
        User author = findUserOrThrow(authorId);
        log.info("🔧 사용자 조회 성공 - 사용자명: {}", author.getUsername());

        List<BinaryContent> savedAttachmentEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(attachments)) {
            for (MultipartFile file : attachments) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                try {
                    BinaryContentDto attachmentDto = binaryContentService.create(file);

                    BinaryContent savedAttachment = binaryContentRepository.findById(
                        attachmentDto.id()).orElseThrow(MessageFileProcessingException::new);
                    savedAttachmentEntities.add(savedAttachment);
                } catch (Exception e) {
                    throw new MessageFileProcessingException();
                }
            }
        }
        Message message = new Message(messageCreateRequest.content(), channel, author,
            savedAttachmentEntities);

        Message savedMessage = messageRepository.save(message);
        log.info("메시지 생성 완료");

        try {
            return messageMapper.toDto(savedMessage);
        } catch (Exception e) {
            binaryContentService.delete(savedMessage.getAttachments().get(0).getId());
            throw new MessageFileProcessingException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(messageId));

        log.info("메시지 조회 완료");
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, String cursor, int size) {
        if (!channelRepository.existsById(channelId)) {
            throw new ChannelNotFoundException(channelId);
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
        log.info("메시지 목록 조회 완료");
        return pageMapper.messageListToPageResponse(pageContent, size, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public Instant lastMessageTime(UUID channelId) {
        return messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channelId)
            .map(Message::getCreatedAt).orElse(Instant.now());
    }


    @PreAuthorize("@messageRepository.findById(#messageId).orElse(null)?.author?.id == authentication.principal.user.id")
    @Override
    @Transactional
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(messageId));

        String newContent = request.newContent();
        message.update(newContent);
        Message updatedMessage = messageRepository.save(message);
        log.info("메시지 수정 완료");
        return messageMapper.toDto(updatedMessage);
    }

    @PreAuthorize("@messageRepository.findById(#messageId).orElse(null)?.author?.id == authentication.principal.user.id or hasRole('ADMIN')")
    @Override
    @Transactional
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new MessageNotFoundException(messageId);
        }
        Message messageToDelete = messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(messageId));

        List<BinaryContent> attachments = messageToDelete.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (BinaryContent attachment : attachments) {
                try {
                    binaryContentService.delete(attachment.getId());
                } catch (Exception e) {
                    log.error("메시지 삭제 중 파일 삭제 실패");
                }
            }
        }
        messageRepository.deleteById(messageId);
    }

    private Channel findChannelOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new ChannelNotFoundException(channelId));
    }

    private User findUserOrThrow(UUID userId) {
        return userRepository.findById(userId).
            orElseThrow(() -> new UserNotFoundException(userId));
    }
}