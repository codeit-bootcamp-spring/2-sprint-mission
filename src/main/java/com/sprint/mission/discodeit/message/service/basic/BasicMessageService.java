package com.sprint.mission.discodeit.message.service.basic;

import com.sprint.mission.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentCore;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.message.dto.request.ChannelMessagePageRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.message.mapper.MessageResultMapper;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.message.service.MessageService;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageResponse<MessageResult> getAllByChannelId(ChannelMessagePageRequest channelMessagePageRequest) {
        log.debug("채널별 메시지 목록 조회 요청: channelId={}, size={}", channelMessagePageRequest.channelId(), channelMessagePageRequest.size());
        Pageable page = Pageable.ofSize(channelMessagePageRequest.size());
        Slice<Message> messages = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelMessagePageRequest.channelId(), page);
        log.info("채널별 메시지 목록 조회 성공: channelId={}, 조회 메시지 수={}", channelMessagePageRequest.channelId(), messages.getNumberOfElements());

        return PageResponse.of(messages, messageResultMapper::convertToMessageResult);
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

}
