package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Override
    public Message create(MessageCreateDto messageCreateDto) {
        try {
            userService.findById(messageCreateDto.authorId());
            channelService.findById(messageCreateDto.channelId());
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Message 생성 실패: " + e.getMessage());
        }

        Message newMessage = new Message(messageCreateDto.authorId(), messageCreateDto.channelId(),
                messageCreateDto.content(), messageCreateDto.attachmentIds());

        return messageRepository.save(newMessage);
    }

    @Override
    public Message findById(UUID messageId) {
        Message message = messageRepository.findById(messageId);

        if (message == null) {
            throw new NoSuchElementException(messageId + " 메시지를 찾을 수 없습니다.");
        }

        return message;
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId);
    }

    @Override
    public List<Message> findByAuthorId(UUID authorId) {
        return messageRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = findById(messageId);
        message.update(newContent);

        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = findById(messageId);
        messageRepository.delete(message.getId());
    }
}
