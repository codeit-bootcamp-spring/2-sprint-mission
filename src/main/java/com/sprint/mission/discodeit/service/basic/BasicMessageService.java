package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Override
    public Message createMessage(CreateMessageRequest request) {
        if (request.userId() == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (request.channelId() == null) {
            throw new IllegalArgumentException("채널 ID는 null일 수 없습니다.");
        }
        if (!userService.getUserById(request.userId()).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + request.userId());
        }
        if (!channelService.getChannelById(request.channelId()).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + request.channelId());
        }

        List<UUID> attachmentIds = new ArrayList<>();
        if (request.attachmentIds() != null && !request.attachmentIds().isEmpty()) {
            for (UUID fileId : request.attachmentIds()) {
                Optional<BinaryContent> file = binaryContentRepository.getById(fileId);
                file.ifPresent(binaryContent -> attachmentIds.add(binaryContent.getId()));
            }
        }

        Message message = new Message(request.userId(), request.channelId(), request.text(), attachmentIds);
        messageRepository.save(message);
        return message;
    }

    @Override
    public Optional<Message> getMessageById(UUID messageId) {
        return messageRepository.getMessageById(messageId);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.getAllMessagesByChannel(channelId);
    }

    @Override
    public void updateMessage(UpdateMessageRequest request) {
        messageRepository.getMessageById(request.messageId()).ifPresent(message -> {
            Instant updatedTime = Instant.now();
            message.update(request.newText(), updatedTime);
            messageRepository.save(message);
        });
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.getMessageById(messageId).ifPresent(message -> {
            message.getAttachmentIds().forEach(binaryContentRepository::deleteById);
            messageRepository.deleteMessage(messageId);
        });
    }
}
