package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageByChannelRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.service.message.MessageResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageResultMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;

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

        return messageResultMapper.convertToMessageResult(savedMessage, user);
    }

    @Override
    public MessageResult getById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        return messageResultMapper.convertToMessageResult(message, message.getUser());
    }

    @Override
    public PageResponse<MessageResult> getAllByChannelId(MessageByChannelRequest messageByChannelRequest) {
        Pageable page = Pageable.ofSize(messageByChannelRequest.size());

        Slice<Message> messages = messageRepository.findByChannelIdOrderByCreatedAtDesc(
                messageByChannelRequest.channelId(), page);

        return PageResponse.of(messages, message ->
                messageResultMapper.convertToMessageResult(message, message.getUser())
        );
    }

    @Override
    public MessageResult updateContext(UUID id, String context) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        message.updateContext(context);
        Message savedMessage = messageRepository.save(message);

        return messageResultMapper.convertToMessageResult(message, savedMessage.getUser());
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        List<UUID> attachmentIds = message.getAttachments()
                .stream()
                .map(BinaryContent::getId)
                .toList();

        binaryContentStorageService.deleteBinaryContentsBatch(attachmentIds);
        messageRepository.deleteById(id);
    }

}
