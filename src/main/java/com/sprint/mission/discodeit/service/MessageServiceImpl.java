package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    public MessageServiceImpl(MessageRepository messageRepository, ChannelRepository channelRepository,
                              UserRepository userRepository, BinaryContentRepository binaryContentRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public Message create(String content, UUID channelId, UUID senderId) {
        Message message = new Message(channelId, senderId, content);
        return messageRepository.save(message);
    }

    @Override
    public Message createMessage(MessageCreateRequestDTO dto) {
        if (!channelRepository.existsById(dto.getChannelId())) {
            throw new RuntimeException("채널을 찾을 수 없습니다.");
        }
        if (!userRepository.existsById(dto.getSenderId())) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        Message message = new Message(dto.getChannelId(), dto.getSenderId(), dto.getContent());
        Message savedMessage = messageRepository.save(message);

        if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
            List<BinaryContent> files = dto.getAttachments().stream()
                    .map(data -> new BinaryContent(UUID.randomUUID(), dto.getSenderId(), savedMessage.getId(), data))
                    .collect(Collectors.toList());
            binaryContentRepository.saveAll(files);
        }

        return savedMessage;
    }

    @Override
    public Optional<Message> find(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public Message updateMessage(MessageUpdateRequestDTO dto) {
        Message message = messageRepository.findById(dto.getMessageId())
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다: " + dto.getMessageId()));

        message.setContent(dto.getNewContent());

        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));
        message.setContent(newContent);
        messageRepository.save(message);
        return message;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        message.setContent(newContent); // 메시지 내용 변경
        return messageRepository.save(message); // 변경된 메시지 저장
    }

    @Override
    public void delete(UUID messageId) {
        binaryContentRepository.deleteByMessageId(messageId);
        messageRepository.deleteById(messageId);
    }
}