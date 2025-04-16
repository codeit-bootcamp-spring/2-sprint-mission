package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.handler.MessageNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Override
    public Message createMessage(CreateMessageRequest request, List<MultipartFile> attachments) {
        if (request.authorId() == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (request.channelId() == null) {
            throw new IllegalArgumentException("채널 ID는 null일 수 없습니다.");
        }

        if (!userService.getUserById(request.authorId()).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + request.authorId());
        }
        if (!channelService.getChannelById(request.channelId()).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + request.channelId());
        }

        List<UUID> attachmentIds = new ArrayList<>();

        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                try {
                    BinaryContent content = new BinaryContent(
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType(),
                        file.getBytes()
                    );
                    binaryContentRepository.save(content);
                    attachmentIds.add(content.getId());
                } catch (Exception e) {
                    throw new RuntimeException("첨부 파일 저장 실패", e);
                }
            }
        }

        Message message = new Message(
            request.authorId(),
            request.channelId(),
            request.content(),
            attachmentIds
        );
        messageRepository.save(message);
        return message;
    }

    @Override
    public MessageResponse getMessageById(UUID messageId) {
        return messageRepository.getMessageById(messageId)
            .map(message -> new MessageResponse(
                message.getId(),
                message.getUserId(),
                message.getChannelId(),
                message.getText(),
                message.getAttachmentIds(),
                message.getCreatedAt(),
                message.getUpdatedAt()
            ))
            .orElseThrow(() -> new MessageNotFoundException(messageId.toString()));
    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        return messageRepository.getAllMessagesByChannel(channelId).stream()
            .map(message -> new MessageResponse(
                message.getId(),
                message.getUserId(),
                message.getChannelId(),
                message.getText(),
                message.getAttachmentIds(),
                message.getCreatedAt(),
                message.getUpdatedAt()
            ))
            .toList();
    }

    @Override
    public void updateMessage(UpdateMessageRequest request) {
        messageRepository.getMessageById(request.messageId()).ifPresent(message -> {
            Instant updatedTime = Instant.now();
            message.update(request.newContent(), updatedTime);
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
