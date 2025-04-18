package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.handler.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    @Transactional
    public Message createMessage(CreateMessageRequest request, List<MultipartFile> attachments) {
        if (request.authorId() == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (request.channelId() == null) {
            throw new IllegalArgumentException("채널 ID는 null일 수 없습니다.");
        }

        User author = userRepository.findById(request.authorId())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + request.authorId()));

        Channel channel = channelRepository.findById(request.channelId())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채널입니다: " + request.channelId()));

        List<BinaryContent> attachmentIds = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                try {
                    BinaryContent content = new BinaryContent(
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType()
                    );
                    binaryContentRepository.save(content);
                    attachmentIds.add(content);
                } catch (Exception e) {
                    throw new RuntimeException("첨부 파일 저장 실패", e);
                }
            }
        }

        Message message = new Message(author, channel, request.content(), attachmentIds);
        return messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public MessageResponse getMessageById(UUID messageId) {
        return messageRepository.findById(messageId)
            .map(this::toResponse)
            .orElseThrow(() -> new MessageNotFoundException(messageId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId));

        return messageRepository.findAllByChannel(channel).stream()
            .map(this::toResponse)
            .toList();
    }

    private MessageResponse toResponse(Message message) {
        List<UUID> attachmentIds = message.getAttachments().stream()
            .map(BinaryContent::getId)
            .toList();

        return new MessageResponse(
            message.getId(),
            message.getAuthor().getId(),
            message.getChannel().getId(),
            message.getContent(),
            attachmentIds,
            message.getCreatedAt(),
            message.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public void updateMessage(UpdateMessageRequest request) {
        messageRepository.findById(request.messageId()).ifPresent(message -> {
            message.update(request.newContent(), message.getAttachments());
            messageRepository.save(message);
        });
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.getAttachments().forEach(binaryContentRepository::delete);
            messageRepository.deleteById(messageId);
        });
    }
}
