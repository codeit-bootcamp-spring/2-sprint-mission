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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Use Spring's Transactional
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(BasicMessageService.class);

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageMapper;


    @Transactional
    @Override
    public MessageDto create(MessageCreateRequest messageCreateRequest,
        List<BinaryContentCreateRequest> binaryContentCreateRequests) {

        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        Channel channel = findChannelOrThrow(channelId);
        User author = findUserOrThrow(authorId);

        List<BinaryContent> savedAttachmentEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(binaryContentCreateRequests)) {
            for (BinaryContentCreateRequest request : binaryContentCreateRequests) {
                if (request == null || request.bytes().length == 0) {
                    continue;
                }

                byte[] fileData = request.bytes();
                Long fileSize = (long) fileData.length;
                String fileName = request.fileName();
                String contentType = request.contentType();

                try {
                    // 콘텐츠 생성
                    BinaryContent metadata = new BinaryContent();
                    metadata.setFileName(fileName);
                    metadata.setContentType(contentType);
                    metadata.setSize(fileSize);

                    //실제 저장
                    binaryContentStorage.put(metadata.getId(), fileData);
                    //메타데이터 저장
                    BinaryContent savedMetadata = binaryContentRepository.save(metadata);

                    savedAttachmentEntities.add(savedMetadata);

                } catch (Exception e) {
                    throw new RuntimeException("첨부 파일 '" + fileName + "' 처리 중 오류가 발생했습니다.", e);
                }
            }
        } else {
            log.info("첨부 파일 없음 (M-N).");
        }
        //메세지 생성 및 저장
        Message message = new Message(
            messageCreateRequest.content(),
            channel,
            author,
            savedAttachmentEntities
        );

        Message savedMessage = messageRepository.save(message);

        return messageMapper.toDto(savedMessage);
    }


    @Override
    @Transactional(readOnly = true)
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
            .orElseThrow(
                () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {

        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }

        Page<Message> messagePage = messageRepository.findAllByChannelId(channelId, pageable);

        PageResponse<MessageDto> response = pageMapper.fromPage(messagePage, messageMapper::toDto);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Instant lastMessageTime(UUID channelId) {
        return messageRepository
            .findTopByChannelIdOrderByCreatedAtDesc(channelId)
            .map(Message::getCreatedAt)
            .orElseThrow(() -> new NoSuchElementException("채널에 메시지가 존재하지 않습니다: ID=" + channelId));
    }

    @Override
    @Transactional
    public Message update(UUID messageId, MessageUpdateRequest request) {
        String newContent = request.newContent();
        Message message = messageRepository.findById(messageId)
            .orElseThrow(
                () -> new NoSuchElementException("Message with id " + messageId + " not found"));

        message.update(newContent);
        Message updatedMessage = messageRepository.save(message);
        log.info("메시지 업데이트 완료: ID={}", messageId);
        return updatedMessage;
    }

    @Override
    @Transactional
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException(
                "Message with id " + messageId + " not found for deletion");
        }
        messageRepository.deleteById(messageId);
    }


    private Channel findChannelOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() ->
                new NoSuchElementException("채널을 찾을 수 없습니다"));
    }

    private User findUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> {

                return new NoSuchElementException("작성자를 찾을 수 없습니다");
            });
    }

}