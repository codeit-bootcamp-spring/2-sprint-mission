package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    public Message createMessage(MessageCreateDto createDto) {
        User user = userRepository.findById(createDto.userId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + createDto.userId()));
        Channel channel = channelRepository.findById(createDto.channelId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 채널을 찾을 수 없습니다: " + createDto.channelId()));

        validateWritePermission(user, channel.getWritePermission());

        List<UUID> updatedAttachmentIds = createDto.attachmentIds() != null ? new ArrayList<>(createDto.attachmentIds()) : new ArrayList<>();
        if (createDto.attachments() != null && !createDto.attachments().isEmpty()) {
            updatedAttachmentIds.addAll(createDto.attachments().stream()
                    .map(file -> {
                        try {
                            Path directory = Paths.get(System.getProperty("user.dir"), "data", "binaryContent");
                            String filePath = directory.resolve(file.getOriginalFilename()).toString();
                            BinaryContentDto binaryContentDto = BinaryContentDto.fromMultipartFile(file, filePath);
                            BinaryContent binaryContent = binaryContentDto.convertToBinaryContent();
                            BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                            return createdBinaryContent.getId();
                        } catch (IOException e) {
                            throw new RuntimeException("파일을 읽는 도중 오류 발생", e);
                        }
                    })
                    .collect(Collectors.toList()));
        }

        return messageRepository.save(createDto.convertCreateDtoToMessage(updatedAttachmentIds));
    }

    @Override
    public MessageResponseDto findById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + id));

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
    public Message updateMessage(MessageUpdateDto updateDto) {
        User user = userRepository.findById(updateDto.userId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + updateDto.userId()));
        Message message = messageRepository.findById(updateDto.id())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + updateDto.id()));
        ensureMessageBelongsToChannel(message, updateDto.channelId());
        validateEditDeletePermission(user, message.getUserId());

       List<UUID> updatedAttachmentIds = updateDto.attachmentIds() != null ? new ArrayList<>(updateDto.attachmentIds()) : new ArrayList<>();
        if ((updateDto.attachments() != null && !updateDto.attachments().isEmpty())) {
            if (updateDto.attachmentIds() != null && !updateDto.attachmentIds().isEmpty()) {
                updateDto.attachmentIds()
                        .forEach(binaryContentRepository::deleteById);
            }
            updatedAttachmentIds.addAll(updateDto.attachments().stream()
                    .map(file -> {
                        try {
                            Path directory = Paths.get(System.getProperty("user.dir"), "data", "binaryContent");
                            String filePath = directory.resolve(file.getOriginalFilename()).toString();
                            BinaryContentDto binaryContentDto = BinaryContentDto.fromMultipartFile(file, filePath);
                            BinaryContent binaryContent = binaryContentDto.convertToBinaryContent();
                            BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                            return createdBinaryContent.getId();
                        } catch (IOException e) {
                            throw new RuntimeException("파일을 읽는 도중 오류 발생", e);
                        }
                    })
                    .collect(Collectors.toList()));
        }

        message.update(updateDto.content(), updatedAttachmentIds);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID id, UUID userId, UUID channelId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 메시지를 찾을 수 없습니다: " + id));
        ensureMessageBelongsToChannel(message, channelId);
        validateEditDeletePermission(user, message.getUserId());

        if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            message.getAttachmentIds()
                    .forEach(binaryContentRepository::deleteById);
        }

        messageRepository.deleteById(id);
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
            throw new IllegalStateException("메시지를 작성할 권한이 없습니다.");
        }
    }

    private void validateEditDeletePermission(User user, UUID messageUserId) {
        if (!user.getId().equals(messageUserId) && user.getRole() != UserRole.ADMIN) {
            throw new IllegalStateException("메시지를 수정/삭제할 권한이 없습니다.");
        }
    }

}
