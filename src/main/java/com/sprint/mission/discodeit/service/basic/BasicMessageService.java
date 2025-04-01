package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.handler.custom.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.handler.custom.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.handler.custom.user.UserNotFoundException;
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
        try {
            userService.findById(messageCreateDto.authorId());
            channelService.findById(messageCreateDto.channelId());
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("Message 생성 실패: " + e.getMessage());
        } catch (ChannelNotFoundException e) {
            throw new ChannelNotFoundException("Message 생성 실패: " + e.getMessage());
        }

        List<UUID> attachmentIds = binaryContentCreateDtos.stream()
                .map(attachmentRequest -> {
                    String fileName = attachmentRequest.fileName();
                    String contentType = attachmentRequest.contentType();
                    byte[] bytes = attachmentRequest.bytesImage();

                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    return createdBinaryContent.getId();
                })
                .toList();

        Message newMessage = new Message(messageCreateDto.authorId(), messageCreateDto.channelId(),
                messageCreateDto.content(), attachmentIds);

        return messageRepository.save(newMessage);
    }

    @Override
    public Message findById(UUID messageId) {
        Message message = messageRepository.findById(messageId);

        if (message == null) {
            throw new MessageNotFoundException(messageId + " 메시지를 찾을 수 없습니다.");
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
    public Message update(MessageUpdateDto messageUpdateDto) {
        Message message = findById(messageUpdateDto.id());
        message.update(messageUpdateDto.content());

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
