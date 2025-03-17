package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Override
    public Message createMessage(UUID userId, UUID channelId, String text) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("채널 ID는 null일 수 없습니다.");
        }
        if (!userService.getUserById(userId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + userId);
        }
        if (!channelService.getChannelById(channelId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId);
        }
        Message message = new Message(userId, channelId, text);
        messageRepository.save(message);
        return message;
    }

    @Override
    public Optional<Message> getMessageById(UUID messageId) {
        return messageRepository.getMessageById(messageId);
    }

    @Override
    public List<Message> getAllMessagesByChannel(UUID channelId) {
        return messageRepository.getAllMessagesByChannel(channelId);
    }

    @Override
    public void updateMessage(UUID messageId, String newText) {
        messageRepository.getMessageById(messageId).ifPresent(message -> {
            Instant updatedTime = Instant.now();
            message.update(newText, updatedTime);
            messageRepository.save(message);
        });
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.deleteMessage(messageId);
    }
}
