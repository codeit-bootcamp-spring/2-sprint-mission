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
        log.info("메시지 생성 시작. 채널 ID: '{}', 작성자 ID: '{}'", messageCreateRequest.channelId(),
            messageCreateRequest.authorId());

        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        Channel channel = findChannelOrThrow(channelId);
        User author = findUserOrThrow(authorId);
        log.info("메시지 생성을 위한 채널(ID: '{}') 및 작성자(ID: '{}') 확인 완료.", channel.getId(), author.getId());

        List<BinaryContent> savedAttachmentEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(binaryContentCreateRequests)) {
            log.info("첨부파일 {}개 처리 시작.", binaryContentCreateRequests.size());
            for (int i = 0; i < binaryContentCreateRequests.size(); i++) {
                BinaryContentCreateRequest request = binaryContentCreateRequests.get(i);
                if (request == null || request.bytes() == null || request.bytes().length == 0) {
                    log.warn(" 요청된 첨부파일이 비어 있습니다.");
                    continue;
                }

                String fileName = request.fileName();

                try {
                    BinaryContent metadata = new BinaryContent();
                    metadata.setFileName(fileName);
                    metadata.setContentType(request.contentType());
                    metadata.setSize((long) request.bytes().length);

                    BinaryContent savedMetadata = binaryContentRepository.save(metadata);
                    binaryContentStorage.put(savedMetadata.getId(), request.bytes());
                    savedAttachmentEntities.add(savedMetadata);
                    log.info("첨부파일 #{} ('{}') 저장 완료. 메타데이터 ID: '{}'", i + 1, fileName,
                        savedMetadata.getId());
                } catch (Exception e) {
                    log.error("첨부파일 '{}' 처리 중 오류 발생", fileName, e);
                    throw new RuntimeException("첨부파일 '" + fileName + "' 처리 중 오류가 발생했습니다.", e);
                }
            }
            log.info("총 {}개의 첨부파일 처리 완료.", savedAttachmentEntities.size());
        } else {
            log.info("요청된 첨부파일 없음.");
        }

        Message message = new Message(
            messageCreateRequest.content(),
            channel,
            author,
            savedAttachmentEntities
        );

        Message savedMessage = messageRepository.save(message);
        log.info("메시지 생성 및 저장 완료. 메시지 ID: '{}', 채널 ID: '{}'", savedMessage.getId(), channelId);
        return messageMapper.toDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto find(UUID messageId) {
        log.info("메시지 조회 시도. ID: '{}'", messageId);
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> {
                log.warn("ID '{}'에 해당하는 메시지를 찾을 수 없습니다.", messageId);
                return new NoSuchElementException("메시지 ID " + messageId + "를 찾을 수 없습니다.");
            });
        log.info("메시지 조회 성공. ID: '{}'", messageId);
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, String cursor, int size) {
        log.info("채널 ID '{}'의 메시지 목록 조회 시도. 커서: '{}', 크기: {}", channelId, cursor, size);
        if (!channelRepository.existsById(channelId)) {
            log.warn("채널 ID '{}'를 찾을 수 없어 메시지 목록 조회를 중단합니다.", channelId);
            throw new NoSuchElementException("채널 ID " + channelId + "를 찾을 수 없습니다.");
        }

        List<Message> messages;
        if (cursor != null) {
            Instant cursorTime = Instant.parse(cursor);
            log.info("커서 시간 '{}' 기준으로 메시지 조회", cursorTime);
            messages = messageRepository.findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(
                channelId, cursorTime, PageRequest.of(0, size + 1));
        } else {
            log.info("최신 메시지부터 조회");
            messages = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId,
                PageRequest.of(0, size + 1));
        }

        boolean hasNext = messages.size() > size;
        List<Message> pageContent = hasNext ? messages.subList(0, size) : messages;
        log.info("채널 ID '{}'의 메시지 {}개 조회 완료. 다음 페이지 존재 여부: {}", channelId, pageContent.size(),
            hasNext);
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
        log.info("메시지 업데이트 시작. ID: '{}'", messageId);
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> {
                log.warn("ID '{}'에 해당하는 업데이트할 메시지를 찾을 수 없습니다.", messageId);
                return new NoSuchElementException("메시지 ID " + messageId + "를 찾을 수 없습니다.");
            });

        String oldContent = message.getContent();
        String newContent = request.newContent();
        message.update(newContent);
        Message updatedMessage = messageRepository.save(message);
        log.info("메시지 업데이트 완료. ID: '{}'. 이전 내용 길이: {}, 새 내용 길이: {}",
            messageId, oldContent != null ? oldContent.length() : 0,
            newContent != null ? newContent.length() : 0);
        return messageMapper.toDto(updatedMessage);
    }

    @Override
    @Transactional
    public void delete(UUID messageId) {
        log.info("메시지 삭제 시작. ID: '{}'", messageId);
        if (!messageRepository.existsById(messageId)) {
            log.warn("ID '{}'에 해당하는 삭제할 메시지를 찾을 수 없습니다.", messageId);
            throw new NoSuchElementException("메시지 ID " + messageId + "를 찾을 수 없습니다.");
        }
        messageRepository.deleteById(messageId);
        log.info("메시지 삭제 완료. ID: '{}'", messageId);
    }

    private Channel findChannelOrThrow(UUID channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> {
                log.warn("메시지 작업 실패: 채널 ID '{}'를 찾을 수 없습니다.", channelId);
                return new NoSuchElementException("채널 ID " + channelId + "를 찾을 수 없습니다.");
            });
    }

    private User findUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("메시지 작업 실패: 사용자(작성자) ID '{}'를 찾을 수 없습니다.", userId);
                return new NoSuchElementException("사용자(작성자) ID " + userId + "를 찾을 수 없습니다.");
            });
    }
}