package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.application.dto.message.MessageResult;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_MESSAGE_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public MessageResult create(MessageCreateRequest messageCreateRequest,
                                List<BinaryContentRequest> files) {

        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 채널이 존재하지 않습니다."));

        List<BinaryContent> attachments;
        if (files != null) {
            attachments = files.stream()
                    .map(binaryContentRequest -> {
                        BinaryContent binaryContent = new BinaryContent(
                                binaryContentRequest.fileName(),
                                binaryContentRequest.contentType());

                        binaryContentStorage.put(binaryContent.getId(), binaryContentRequest.bytes());

                        return binaryContentRepository.save(binaryContent);
                    })
                    .toList();
        } else {
            attachments = List.of();
        }

        User user = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        Message message = messageRepository.save(
                new Message(channel, user, messageCreateRequest.content(), attachments));

        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return MessageResult.fromEntity(message, UserResult.fromEntity(user, userStatus.isOnline(Instant.now())));
    }

    @Transactional(readOnly = true)
    @Override
    public MessageResult getById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        User user = message.getUser();
        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return MessageResult.fromEntity(message, UserResult.fromEntity(user, userStatus.isOnline(Instant.now())));
    }

    @Transactional(readOnly = true)
    @Override
    public List<MessageResult> getAllByChannelId(UUID channelId) {
        return messageRepository.findAll()
                .stream()
                .filter(message -> message.getChannel().getId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(message -> {
                    User user = message.getUser();
                    UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

                    return MessageResult.fromEntity(message, UserResult.fromEntity(user, userStatus.isOnline(Instant.now())));
                })
                .toList();
    }

    @Transactional
    @Override
    public MessageResult updateContext(UUID id, String context) {
        Message message = messageRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));

        message.updateContext(context);

        User user = message.getUser();
        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return MessageResult.fromEntity(message, UserResult.fromEntity(user, userStatus.isOnline(Instant.now())));
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(ERROR_MESSAGE_NOT_FOUND.getMessageContent()));


        List<UUID> attachmentIds = message.getAttachments()
                .stream()
                .map(BinaryContent::getId)
                .toList();

        for (UUID attachmentId : attachmentIds) {
            binaryContentRepository.deleteById(attachmentId);
        }

        messageRepository.deleteById(id);
    }
}
