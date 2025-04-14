package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.LogicException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos) {
        userService.findById(messageCreateDto.authorId());
        channelService.findById(messageCreateDto.channelId());

        List<BinaryContent> binaryContents = binaryContentCreateDtos.stream()
                .map(dto -> new BinaryContent(
                        dto.fileName(),
                        (long) dto.bytes().length,
                        dto.contentType(),
                        dto.bytes()
                ))
                .toList();

        List<BinaryContent> savedBinaryContents = binaryContents.stream()
                .map(binaryContentRepository::save)
                .toList();

        List<UUID> attachmentIds = savedBinaryContents.stream()
                .map(BinaryContent::getId)
                .toList();

        Message newMessage = new Message(messageCreateDto.authorId(), messageCreateDto.channelId(),
                messageCreateDto.content(), attachmentIds);

        return messageRepository.save(newMessage);
    }

    @Override
    public Message findById(UUID messageId) {
        Message message = messageRepository.findById(messageId);

        if (message == null) {
            throw new LogicException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public List<Message> findAllByAuthorId(UUID authorId) {
        return messageRepository.findAllByAuthorId(authorId);
    }

    @Override
    public Message update(UUID messageId, MessageUpdateDto messageUpdateDto) {
        Message message = findById(messageId);
        message.update(messageUpdateDto.newContent());

        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = findById(messageId);
        List<UUID> attachmentIds = message.getAttachmentIds();

        if (!attachmentIds.isEmpty()) {
            attachmentIds.forEach(binaryContentRepository::delete);
        }

        messageRepository.delete(message.getId());
    }
}
