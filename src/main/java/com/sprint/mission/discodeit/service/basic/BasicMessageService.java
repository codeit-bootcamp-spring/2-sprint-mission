package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UUID create(CreateMessageRequest request) {
        validateContent(request.getContent());

        String content = request.getContent();
        UUID senderId = request.getAuthorId();
        UUID channelId = request.getChannelId();

        Message message = new Message(content, senderId, channelId);
        messageRepository.save(message);

        return message.getId();
    }

    @Override
    public UUID create(CreateMessageRequest request,
        List<CreateBinaryContentRequest> binaryContents) {

        validateContent(request.getContent());

        String content = request.getContent();
        UUID senderId = request.getAuthorId();
        UUID channelId = request.getChannelId();

        Message message = new Message(content, senderId, channelId);
        messageRepository.save(message);

        if (binaryContents == null || binaryContents.isEmpty()) {
            throw new IllegalArgumentException("[ERROR]binary content is empty");
        }

        List<UUID> binaryContentIds = new ArrayList<>();
        for (CreateBinaryContentRequest binaryContentDto : binaryContents) {
            String name = binaryContentDto.getFileName();
            String contentType = binaryContentDto.getContentType();
            byte[] bytes = binaryContentDto.getBytes();
            int size = bytes.length;

            BinaryContent binaryContent = new BinaryContent(
                name,
                size,
                contentType,
                bytes
            );

            binaryContentIds.add(binaryContent.getId());
            binaryContentRepository.save(binaryContent);
        }

        update(message.getId(),
            new UpdateMessageRequest(message.getContent(), binaryContentIds));

        return message.getId();
    }

    @Override
    public MessageResponseDto findById(UUID messageId) {
        Message message = messageRepository.findByMessageId(messageId);
        MessageResponseDto messageResponseDto = MessageResponseDto.from(message);

        return messageResponseDto;
    }

    @Override
    public List<MessageResponseDto> findAll() {
        return messageRepository.findAll().stream()
            .map(MessageResponseDto::from).toList();
    }

    @Override
    public List<MessageResponseDto> findByChannelId(UUID channelId) {
        return messageRepository.findAll().stream()
            .filter(m -> m.getChannelId().equals(channelId))
            .map(MessageResponseDto::from).toList();
    }

    @Override
    public void update(UUID messageId, UpdateMessageRequest request) {
        Message message = messageRepository.findByMessageId(messageId);

        if (message == null) {
            throw new IllegalArgumentException("[ERROR] message id not found");
        }

        String content = request.getContent();
        message.updateContent(content);

        List<UUID> binaryContentIds = request.getBinaryContentIds();
        if (binaryContentIds == null || binaryContentIds.isEmpty()) {
            return;
        }

        for (UUID binaryContentId : binaryContentIds) {
            message.updateImages(binaryContentId);
        }

        messageRepository.save(message);
    }

    @Override
    public void remove(UUID messageId) {
        Message message = messageRepository.findByMessageId(messageId);

        List<UUID> attachedImageIds = message.getAttachedImageIds();
        for (UUID attachedImageId : attachedImageIds) {
            binaryContentRepository.delete(attachedImageId);
        }

        messageRepository.delete(messageId);
    }

    private void validateContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("메시지 내용이 없습니다. 메시지를 입력해주세요.");
        }
    }
}
