package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentStorageService;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.common.dto.response.PageResponse;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.message.dto.request.ChannelMessagePageRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.mapper.MessageResultMapper;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.common.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;
import static com.sprint.mission.discodeit.common.constant.ErrorMessages.ERROR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentStorageService binaryContentStorageService;
    private final MessageResultMapper messageResultMapper;

    @Override
    public MessageResult create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> files) {
        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 채널이 존재하지 않습니다."));
        User user = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        List<BinaryContent> attachments = binaryContentStorageService.createBinaryContents(files);
        Message savedMessage = messageRepository.save(new Message(channel, user, messageCreateRequest.content(), attachments));

        return messageResultMapper.convertToMessageResult(savedMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageResult getById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        return messageResultMapper.convertToMessageResult(message);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageResult> getAllByChannelId(ChannelMessagePageRequest channelMessagePageRequest) {
        Pageable page = Pageable.ofSize(channelMessagePageRequest.size());
        Slice<Message> messages = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelMessagePageRequest.channelId(), page);

        return PageResponse.of(messages, messageResultMapper::convertToMessageResult);
    }

    @Transactional
    @Override
    public MessageResult updateContext(UUID id, String context) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        message.updateContext(context);
        Message savedMessage = messageRepository.save(message);

        return messageResultMapper.convertToMessageResult(savedMessage);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        List<UUID> attachmentIds = message.getAttachments()
                .stream()
                .map(BinaryContent::getId)
                .toList();

        messageRepository.deleteById(id);
//        binaryContentStorageService.deleteBinaryContentsBatch(attachmentIds);
    }

}
