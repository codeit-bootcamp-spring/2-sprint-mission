package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try{
            channelService.find(channelId);
            userService.find(authorId);
        } catch (NoSuchElementException e){
            throw e;
        }

        Message message = new Message(content, channelId, authorId);
        return messageRepository.save(new Message(content, channelId, authorId));
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message existingMessage = messageRepository.findById(messageId);
        existingMessage.update(newContent);
        return messageRepository.save(existingMessage);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
