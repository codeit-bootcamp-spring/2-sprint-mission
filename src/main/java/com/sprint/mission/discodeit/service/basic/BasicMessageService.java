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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


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

    @Autowired
    public BasicMessageService(
        MessageRepository messageRepository,
        ChannelRepository channelRepository,
        UserRepository userRepository,
        BinaryContentRepository binaryContentRepository,
        BinaryContentStorage binaryContentStorage,
        MessageMapper messageMapper,
        @Qualifier("pageResponseMapperImpl") PageResponseMapper pageMapper
    ) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
        this.binaryContentStorage = binaryContentStorage;
        this.messageMapper = messageMapper;
        this.pageMapper = pageMapper;
    }

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

                    BinaryContent savedMetadata = binaryContentRepository.save(metadata);

                    // 생성된 ID로 파일 저장
                    binaryContentStorage.put(savedMetadata.getId(), fileData);

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
    public MessageDto find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(
                () -> new NoSuchElementException("Message with id " + messageId + " not found"));
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, String cursor, int size) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
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
        String newContent = request.newContent();
        Message message = messageRepository.findById(messageId)
            .orElseThrow(
                () -> new NoSuchElementException("Message with id " + messageId + " not found"));

        message.update(newContent);
        Message updatedMessage = messageRepository.save(message);

        return messageMapper.toDto(updatedMessage);
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