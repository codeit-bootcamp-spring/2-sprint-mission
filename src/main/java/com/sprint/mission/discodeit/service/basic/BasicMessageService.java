package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
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
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;


    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        validateMessageInput(content, channelId, authorId);
        Message message = new Message(content, channelId, authorId);
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository
                .findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = find(messageId);
        message.update(newContent);
        messageRepository.save(message);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        find(messageId);
        messageRepository.deleteById(messageId);
    }

    private void validateMessageInput(String content, UUID channelId, UUID authorId) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content는 필수 입력값입니다.");
        }
        if (userRepository.findById(authorId).isEmpty()) {
            throw new NoSuchElementException("유저가 존재하지 않습니다. id : " + authorId);
        }
        if(channelRepository.findById(channelId).isEmpty()) {
            throw new NoSuchElementException("채널이 존재하지 않습니다. id : " + channelId);
        }
    }
}
