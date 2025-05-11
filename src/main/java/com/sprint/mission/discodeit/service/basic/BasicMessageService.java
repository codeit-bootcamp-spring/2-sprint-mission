package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.handler.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageMapper messageMapper;
    private final BinaryContentService binaryContentService;


    @Override
    @Transactional
    public Message createMessage(CreateMessageRequest request, List<MultipartFile> attachments) {
        log.info("메시지 생성 요청 - authorId: {}, channelId: {}", request.authorId(),
            request.channelId());

        if (request.authorId() == null) {
            log.warn("메시지 생성 실패 - 사용자 ID가 null");
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (request.channelId() == null) {
            log.warn("메시지 생성 실패 - 채널 ID가 null");
            throw new IllegalArgumentException("채널 ID는 null일 수 없습니다.");
        }

        User author = userRepository.findById(request.authorId())
            .orElseThrow(() -> {
                log.warn("존재하지 않는 사용자 - userId: {}", request.authorId());
                return new IllegalArgumentException("존재하지 않는 사용자입니다: " + request.authorId());
            });

        Channel channel = channelRepository.findById(request.channelId())
            .orElseThrow(() -> {
                log.warn("존재하지 않는 채널 - channelId: {}", request.channelId());
                return new IllegalArgumentException("존재하지 않는 채널입니다: " + request.channelId());
            });

        List<BinaryContent> attachmentIds = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                try {
                    log.debug("첨부파일 저장 시작 - fileName: {}", file.getOriginalFilename());

                    CreateBinaryContentRequest binaryRequest = new CreateBinaryContentRequest(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                    );

                    BinaryContentDto dto = binaryContentService.create(binaryRequest);
                    BinaryContent content = binaryContentRepository.findById(dto.id())
                        .orElseThrow(() -> new IllegalStateException("저장된 파일을 다시 찾을 수 없습니다."));
                    attachmentIds.add(content);

                    log.debug("첨부파일 저장 완료 - contentId: {}", content.getId());

                } catch (Exception e) {
                    log.error("첨부파일 저장 실패 - fileName: {}", file.getOriginalFilename(), e);
                    throw new RuntimeException("첨부 파일 저장 실패", e);
                }
            }
        }

        Message message = new Message(author, channel, request.content(), attachmentIds);
        log.info("메시지 생성 완료 - messageId: {}", message.getId());
        return messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto getMessageById(UUID messageId) {
        log.info("단일 메시지 조회 요청 - messageId: {}", messageId);
        return messageRepository.findById(messageId)
            .map(messageMapper::toDto)
            .orElseThrow(() -> new MessageNotFoundException(messageId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, int size) {
        log.info("채널 메시지 목록 조회 요청 - channelId: {}, cursor: {}, size: {}", channelId, cursor, size);

        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> {
                log.warn("존재하지 않는 채널 - channelId: {}", channelId);
                return new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId);
            });

        PageRequest pageRequest = PageRequest.of(0, size + 1); // +1로 hasNext 판단
        List<Message> messages;

        if (cursor == null) {
            messages = messageRepository.findFirstPageByChannel(channel, pageRequest);
        } else {
            messages = messageRepository.findNextPageByChannelAndCursor(channel, cursor,
                pageRequest);
        }

        boolean hasNext = messages.size() > size;
        if (hasNext) {
            messages = messages.subList(0, size); // 초과분 제거
        }

        List<MessageDto> messageDtos = messages.stream()
            .map(messageMapper::toDto)
            .toList();

        Instant nextCursor = !messageDtos.isEmpty()
            ? messageDtos.get(messageDtos.size() - 1).createdAt()
            : null;

        return new PageResponse<>(
            messageDtos,
            nextCursor,
            size,
            hasNext,
            null
        );
    }

    @Override
    @Transactional
    public void updateMessage(UpdateMessageRequest request) {
        log.info("메시지 수정 요청 - messageId: {}", request.messageId());

        messageRepository.findById(request.messageId()).ifPresent(message -> {
            message.update(request.newContent(), message.getAttachments());
            messageRepository.save(message);
            log.info("메시지 수정 완료 - messageId: {}", message.getId());
        });
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId) {
        log.info("메시지 삭제 요청 - messageId: {}", messageId);

        messageRepository.findById(messageId).ifPresent(message -> {
            message.getAttachments().forEach(file -> {
                binaryContentRepository.delete(file);
                log.debug("첨부파일 삭제 - fileId: {}", file.getId());
            });
            
            messageRepository.deleteById(messageId);
            log.info("메시지 삭제 완료 - messageId: {}", messageId);
        });
    }
}
