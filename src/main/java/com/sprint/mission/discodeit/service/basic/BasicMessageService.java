package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.event.EventPublisher;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.event.MessageCreatedEvent;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageMapper;
    private final BinaryContentService binaryContentService;
    private final ReadStatusRepository readStatusRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public MessageDto create(MessageCreateRequest request, List<MultipartFile> attachments, User user) {
        Channel channel = getChannel(request);
        List<BinaryContent> attachmentEntities = processAttachments(attachments);

        Message message = saveMessage(request, user, channel, attachmentEntities);

        updateReadStatus(user, channel, message);
        publishMessageCreatedEvent(message);
        
        // 채널의 다른 참여자들에게 새 메시지 알림 전송
        notifyChannelParticipants(channel, user, message);

        return messageMapper.toDto(message);
    }

    private List<BinaryContent> processAttachments(List<MultipartFile> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new ArrayList<>();
        }

        List<BinaryContent> attachmentEntities = new ArrayList<>();
        for (MultipartFile attachment : attachments) {
            if (attachment != null && !attachment.isEmpty()) {
                BinaryContentDto binaryContentDto = binaryContentService.create(attachment);
                BinaryContent binaryContent = binaryContentRepository.findById(binaryContentDto.id())
                        .orElseThrow(FileProcessingCustomException::new);
                attachmentEntities.add(binaryContent);
            }
        }
        return attachmentEntities;
    }

    private Message saveMessage(MessageCreateRequest request, User user, Channel channel, List<BinaryContent> attachmentEntities) {
        Message message = new Message(
                request.content(),
                channel,
                user,
                attachmentEntities
        );
        return messageRepository.save(message);
    }

    private void updateReadStatus(User user, Channel channel, Message message) {
        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(user.getId(), channel.getId())
                .orElse(new ReadStatus(user, channel, message.getCreatedAt()));
        readStatus.update(message.getCreatedAt());
        readStatusRepository.save(readStatus);
    }

    //생성됐다는 것만 이벤트로 날림
    private void publishMessageCreatedEvent(Message message) {
        applicationEventPublisher.publishEvent(new MessageCreatedEvent(message));
    }

    private Channel getChannel(MessageCreateRequest request) {
        return channelRepository.findById(request.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(request.channelId()));
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

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
    public Instant findLastMessageTimestamp(UUID channelId) {
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

    private void notifyChannelParticipants(Channel channel, User sender, Message message) {
        // 채널의 모든 참여자를 ReadStatus를 통해 조회
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channel.getId());
        
        for (ReadStatus readStatus : readStatuses) {
            User participant = readStatus.getUser();
            // 메시지 작성자가 아닌 참여자들에게만 알림 전송
            if (!participant.getId().equals(sender.getId())) {
                String notificationMessage = String.format("%s님이 #%s에 새 메시지를 보냈습니다: %s", 
                    sender.getUsername(), 
                    channel.getName(),
                    message.getContent().length() > 50 ? 
                        message.getContent().substring(0, 50) + "..." : 
                        message.getContent()
                );
                eventPublisher.publishNotification(participant.getId(), notificationMessage);
            }
        }
    }
}