package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageDto messageDto, List<BinaryContentDto> binaryContentDto) {
        String content = messageDto.content();
        UUID channelId = messageDto.channelId();
        UUID authorId = messageDto.authorId();

        if (!userRepository.existsById(authorId)) {
            throw new IllegalArgumentException("로그인 정보가 존재하지 않습니다.");
        }
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException("해당 채널을 찾을 수 없습니다.");
        }

        List<UUID> attachmentIds = binaryContentDto.stream()
                .map(createBinaryContent -> {
                    String fileName = createBinaryContent.fileName();
                    String contentType = createBinaryContent.contentType();
                    byte[] fileData = createBinaryContent.fileData();
                    Long fileSize = (long) fileData.length;

                    BinaryContent binaryContent = new BinaryContent(fileName, fileData, fileSize, contentType);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .collect(Collectors.toList());

        Message message = new Message(content, authorId, channelId, attachmentIds);
        messageRepository.save(message);

        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream().toList();
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다."));
    }

    @Override
    public Message update(UUID messageId, MessageUpdateDto messageUpdateDto) {
        String newContent = messageUpdateDto.newContent();

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다."));

        message.update(newContent);

        messageRepository.save(message);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지를 찾을 수 없습니다."));

        if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            for (UUID attachmentId : message.getAttachmentIds()) {
                binaryContentRepository.deleteById(attachmentId);
            }
        }

        messageRepository.deleteById(messageId);
    }
}
