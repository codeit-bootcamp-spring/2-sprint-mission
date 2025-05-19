package com.sprint.mission.discodeit.domain.message.service.basic;

import com.sprint.mission.discodeit.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentCore;
import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.domain.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.message.dto.MessageResult;
import com.sprint.mission.discodeit.domain.message.dto.request.ChannelMessagePageRequest;
import com.sprint.mission.discodeit.domain.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.domain.message.mapper.MessageResultMapper;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.message.service.MessageService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentCore binaryContentCore;
    private final MessageResultMapper messageResultMapper;

    @Override
    public MessageResult create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> files) {
        log.info("메시지 생성 요청: channelId={}, authorId={}, 첨부파일 수={}", messageCreateRequest.channelId(), messageCreateRequest.authorId(), files != null ? files.size() : 0);
        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(Map.of("channelId", messageCreateRequest.channelId())));
        User user = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> new UserNotFoundException(Map.of("userId", messageCreateRequest.authorId())));

        List<BinaryContent> attachments = binaryContentCore.createBinaryContents(files);
        Message savedMessage = messageRepository.save(new Message(channel, user, messageCreateRequest.content(), attachments));
        log.info("메시지 생성 성공: messageId={}", savedMessage.getId());

        return messageResultMapper.convertToMessageResult(savedMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageResult getById(UUID id) {
        log.debug("메시지 조회 요청: messageId={}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(Map.of("messageId", id)));

        log.info("메시지 조회 성공: messageId={}", id);
        return messageResultMapper.convertToMessageResult(message);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageResult> getAllByChannelId(UUID channelId, ChannelMessagePageRequest channelMessagePageRequest) {
        log.debug("채널별 메시지 목록 조회 요청: channelId={}, pageSize={}", channelId, channelMessagePageRequest.pageSize());
        Instant cursorCreatedAt = getInstant(channelMessagePageRequest);
        Pageable pageable = createPageable(channelMessagePageRequest);

        Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthorDesc(channelId, cursorCreatedAt, pageable);
        log.info("채널별 메시지 목록 조회 성공: channelId={}, 조회 메시지 수={}", channelId, messages.getNumberOfElements());

        return PageResponse.of(messages, messageResultMapper::convertToMessageResult, getNextCursor(messages));
    }

    @Transactional
    @Override
    public MessageResult updateContext(UUID id, String context) {
        log.info("메시지 수정 요청: messageId={}, newContent={}", id, context);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(Map.of("messageId", id)));

        message.updateContext(context);
        Message savedMessage = messageRepository.save(message);
        log.info("메시지 수정 성공: messageId={}", id);

        return messageResultMapper.convertToMessageResult(savedMessage);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        log.warn("메시지 삭제 요청: messageId={}", id);
        if (!messageRepository.existsById(id)) {
            throw new MessageNotFoundException(Map.of("messageId", id));
        }

        messageRepository.deleteById(id);
        log.info("메시지 삭제 성공: messageId={}", id);
    }

    private Pageable createPageable(ChannelMessagePageRequest request) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        return PageRequest.of(request.pageNumber(), request.pageSize(), sort);
    }

    private Instant getInstant(ChannelMessagePageRequest channelMessagePageRequest) {
        if (channelMessagePageRequest.cursor() == null) {
            return Instant.now();
        }
        return channelMessagePageRequest.cursor();
    }

    private Instant getNextCursor(Slice<Message> messages) {
        if (messages.getContent().isEmpty()) {
            return null;
        }
        return messages.getContent().get(messages.getContent().size() - 1).getCreatedAt();
    }

}
