package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.MessageUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void createMessage(MessageCreateDto createDto) {
        User user = userRepository.findById(createDto.userId());
        Channel channel = channelRepository.findById(createDto.channelId());

        validateWritePermission(user, channel.getWritePermission());

        List<UUID> updatedAttachmentIds = createDto.attachmentIds() != null ? new ArrayList<>(createDto.attachmentIds()) : new ArrayList<>();
        if (createDto.attachments() != null && !createDto.attachments().isEmpty()) {
            updatedAttachmentIds.addAll(createDto.attachments().stream()
                    .map(BinaryContentDto::convertToBinaryContent)
                    .map(binaryContentRepository::createBinaryContent)
                    .toList());
        }

        messageRepository.createMessage(createDto.convertCreateDtoToMessage(updatedAttachmentIds));
    }

    @Override
    public MessageResponseDto findById(UUID id) {
        Message message = messageRepository.findById(id);

        return MessageResponseDto.convertToResponseDto(message);
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId)
                .stream()
                .map(MessageResponseDto::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(MessageUpdateDto updateDto) {
        User user = userRepository.findById(updateDto.userId());
        Message message = messageRepository.findById(updateDto.id());
        ensureMessageBelongsToChannel(message, updateDto.channelId());
        validateEditDeletePermission(user, message.getUserId());

        List<UUID> updatedAttachmentIds = updateDto.attachmentIds() != null ? new ArrayList<>(updateDto.attachmentIds()) : new ArrayList<>();
        if ((updateDto.attachments() != null && !updateDto.attachments().isEmpty())) {
            updateDto.attachments().stream()
                    .peek(attachmentDto -> binaryContentRepository.deleteBinaryContent(attachmentDto.contentId()))
                    .map(BinaryContentDto::convertToBinaryContent)
                    .map(binaryContentRepository::createBinaryContent)
                    .forEach(updatedAttachmentIds::add);
        } else {
            updatedAttachmentIds = updateDto.attachmentIds();
        }

        messageRepository.updateMessage(updateDto.id(), updateDto.content(), updateDto.userId(), updateDto.channelId(), updatedAttachmentIds);
    }

    @Override
    public void deleteMessage(UUID id, UUID userId, UUID channelId) {
        User user = userRepository.findById(userId);
        Message message = messageRepository.findById(id);
        ensureMessageBelongsToChannel(message, channelId);
        validateEditDeletePermission(user, message.getUserId());

        if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            message.getAttachmentIds()
                    .forEach(binaryContentRepository::deleteBinaryContent);
        }

        messageRepository.deleteMessage(id, userId, channelId);
    }

    /*******************************
     * Validation check
     *******************************/
    private void ensureMessageBelongsToChannel(Message message, UUID channelId) {
        if (!message.getChannelId().equals(channelId)) {
            throw new RuntimeException("해당 메시지는 요청한 채널에 속하지 않습니다.");
        }
    }

    private void validateWritePermission(User user, UserRole channelRequiredRole) {
        if (user.getRole() != channelRequiredRole && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("메시지를 작성할 권한이 없습니다.");
        }
    }

    private void validateEditDeletePermission(User user, UUID messageUserId) {
        if (!user.getId().equals(messageUserId) && user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("메시지를 수정/삭제할 권한이 없습니다.");
        }
    }

}
