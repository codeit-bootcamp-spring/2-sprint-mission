package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public UUID create(CreateMessageRequest request) {

        UUID authorId = request.getAuthorId();
        UUID channelId = request.getChannelId();
        if (!userRepository.existById(authorId)) {
            throw new NoSuchElementException("[ERROR] user not found");
        }
        if (!channelRepository.existById(channelId)) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }

        String content = request.getContent();
        Message message = new Message(content, authorId, channelId);
        messageRepository.save(message);

        return message.getId();
    }

    @Override
    public UUID create(CreateMessageRequest request,
        List<CreateBinaryContentRequest> binaryContents) {

        UUID authorId = request.getAuthorId();
        UUID channelId = request.getChannelId();
        if (!userRepository.existById(authorId)) {
            throw new NoSuchElementException("[ERROR] user not found");
        }
        if (!channelRepository.existById(channelId)) {
            throw new NoSuchElementException("[ERROR] channel not found");
        }

        String content = request.getContent();
        Message message = new Message(content, authorId, channelId);
        messageRepository.save(message);

        if (binaryContents == null || binaryContents.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] binary content is empty");
        }

        List<UUID> binaryContentIds = new ArrayList<>();
        for (CreateBinaryContentRequest binaryContentDto : binaryContents) {
            String fileName = binaryContentDto.getFileName();
            String contentType = binaryContentDto.getContentType();
            byte[] bytes = binaryContentDto.getBytes();

            BinaryContent binaryContent = new BinaryContent(
                fileName,
                bytes.length,
                contentType,
                bytes
            );

            binaryContentIds.add(binaryContent.getId());
            binaryContentRepository.save(binaryContent);
        }

        for (UUID binaryContentId : binaryContentIds) {
            message.updateImages(binaryContentId);
        }

        return message.getId();
    }

    @Override
    public MessageResponseDto findById(UUID messageId) {
        Message message = messageRepository.findByMessageId(messageId);

        return MessageResponseDto.from(message);
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
            throw new NoSuchElementException("[ERROR] message not found");
        }

        String content = request.getContent();
        message.updateContent(content);

        List<UUID> binaryContentIds = request.getBinaryContentIds();
        if (binaryContentIds == null || binaryContentIds.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] binary content is empty");
        }

        for (UUID binaryContentId : binaryContentIds) {
            message.updateImages(binaryContentId);
        }

        messageRepository.save(message);
    }

    @Override
    public void remove(UUID messageId) {
        Message message = messageRepository.findByMessageId(messageId);
        if (message == null) {
            throw new NoSuchElementException("[ERROR] message not found");
        }

        List<UUID> attachedImageIds = message.getAttachedImageIds();
        for (UUID attachedImageId : attachedImageIds) {
            binaryContentRepository.delete(attachedImageId);
        }

        messageRepository.delete(messageId);
    }
}
