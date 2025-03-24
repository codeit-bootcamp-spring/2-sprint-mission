package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;
    private final BinaryContentRepository binaryContentRepository;

    private void saveMessageData() {
        messageRepository.save();
    }

    @Override
    public Message createMessage(MessageCreateDto dto) {
        UUID channelId = dto.getChannelId();
        Message message = new Message(dto.getUserId(), channelId, dto.getContent());

        channelService.addMessage(channelId, message.getId());

        dto.getFilePath().forEach(filePath ->
                binaryContentRepository.addBinaryContent(
                        new BinaryContent(channelId, filePath)));

        messageRepository.addMessage(message);
        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        validateMessageExists(messageId);
        return messageRepository.findMessageById(messageId);
    }

    @Override
    public List<Message> findMessagesByUserAndChannel(UUID senderId, UUID channelId) {
        userService.validateUserExists(senderId);
        channelService.validateChannelExists(channelId);
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findallByChannelId(UUID channelId) {
        channelService.validateChannelExists(channelId);
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findallByUserId(UUID senderId) {
        userService.validateUserExists(senderId);
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(MessageUpdateDto dto) {
        Message message = messageRepository.findMessageById(dto.getMessageId());
        message.updateContent(dto.getContent());
        saveMessageData();
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findMessageById(messageId);

        channelService.removeMessage(message.getChannelId(), messageId);
        binaryContentRepository.deleteBinaryContent(messageId);

        messageRepository.deleteMessageById(messageId);
    }

    @Override
    public void validateMessageExists(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다.");
        }
    }
}
