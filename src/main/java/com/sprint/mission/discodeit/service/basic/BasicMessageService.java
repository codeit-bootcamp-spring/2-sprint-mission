package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.LogicException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;

    @Transactional
    @Override
    public MessageDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos) {
        User user = userRepository.findById(messageCreateDto.authorId())
                .orElseThrow(() -> new LogicException(ErrorCode.USER_NOT_FOUND));

        Channel channel = channelRepository.findById(messageCreateDto.channelId())
                .orElseThrow(() -> new LogicException(ErrorCode.CHANNEL_NOT_FOUND));

        List<BinaryContent> attachments = new ArrayList<>();

        for (BinaryContentCreateDto dto : binaryContentCreateDtos) {
            BinaryContent binaryContent = new BinaryContent(dto.fileName(), (long) dto.bytes().length,
                    dto.contentType());
            binaryContentRepository.save(binaryContent);
            binaryContentStorage.put(binaryContent.getId(), dto.bytes());
            attachments.add(binaryContent);
        }

        Message newMessage = new Message(user, channel,
                messageCreateDto.content(), attachments);
        messageRepository.save(newMessage);

        return messageMapper.toDto(newMessage);
    }

    @Transactional(readOnly = true)
    @Override
    public MessageDto findById(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new LogicException(ErrorCode.MESSAGE_NOT_FOUND));

        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MessageDto> findAllByAuthorId(UUID authorId) {
        return messageRepository.findAllByAuthorId(authorId).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public MessageDto update(UUID messageId, MessageUpdateDto messageUpdateDto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new LogicException(ErrorCode.MESSAGE_NOT_FOUND));
        message.update(messageUpdateDto.newContent());

        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new LogicException(ErrorCode.MESSAGE_NOT_FOUND));

        messageRepository.delete(message);
    }
}
