package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private final ReadStatusRepository readStatusRepository;

    @Override
    public MessageDto create(MessageCreateRequest request, List<MultipartFile> attachments, User user) {
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(request.channelId()));

        List<BinaryContent> attachmentEntities = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile attachment : attachments) {
                if (!attachment.isEmpty()) {
                    BinaryContentDto binaryContentDto = binaryContentService.create(attachment);
                    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentDto.id())
                            .orElseThrow(FileProcessingCustomException::new);
                    attachmentEntities.add(binaryContent);
                }
            }
        }

        Message message = new Message(
                request.content(),
                channel,
                user,
                attachmentEntities
        );

        Message savedMessage = messageRepository.save(message);

        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(user.getId(), channel.getId())
                .orElse(new ReadStatus(user, channel, savedMessage.getCreatedAt()));
        readStatus.update(savedMessage.getCreatedAt());
        readStatusRepository.save(readStatus);

        return messageMapper.toDto(savedMessage);
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

}