package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.message.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message createMessage(CreateMessageDTO dto, List<BinaryContentDTO> binaryContentDto) {
        UUID channelId = dto.channelId();
        UUID userId = dto.userId();

        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 사용자 입니다. 메세지를 생성할 수 없습니다.");
        }
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("존재하지 않는 채널 입니다. 메세지를 생성할 수 없습니다.");
        }
        List<UUID> attachmentIds = binaryContentDto.stream()
                .map(attachmentRequest -> {
                    String fileName = attachmentRequest.fileName();
                    String contentType = attachmentRequest.contentType();
                    byte[] bytes = attachmentRequest.bytes();

                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    return createdBinaryContent.getId();
                })
                .toList();

        String content = dto.text();
        Message message = new Message(
                content,
                userId,
                channelId,
                attachmentIds
        );

        return messageRepository.save(message);
    }

    @Override
    public Message searchMessage(UUID messageId) {
        return getMessage(messageId);
    }

    @Override
    public List<Message> searchAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .toList();
    }

    @Override
    public Message updateMessage(UUID messageId, UpdateMessageDTO dto) {
        String newText = dto.text();
        Message message = getMessage(messageId);
        message.updateText(newText);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = getMessage(messageId);

        message.getAttachmentIds()
                .forEach(binaryContentRepository::deleteById);

        messageRepository.deleteById(messageId);
    }

    private Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + messageId + "인 메세지를 찾을 수 없습니다."));
    }
}
