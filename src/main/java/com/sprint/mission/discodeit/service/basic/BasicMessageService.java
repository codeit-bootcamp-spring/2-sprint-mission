package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public Message create(String content, UUID channelId, UUID authorId){
        Message message = new Message(content, channelId, authorId);
        return messageRepository.save(message);
    }

    public Message find(UUID messageId){
        return messageRepository.findById(messageId);
    }

    public List<Message> findAll(){
        return messageRepository.findAll();
    }

    public Message update(UUID messageId, String newContent){
        Map<UUID, Message> messageData = messageRepository.getMessageData();

        Message messageNullable = messageData.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        return messageRepository.update(message, newContent);
    }

    public void delete(UUID messageId){
        Map<UUID, Message> messageData = messageRepository.getMessageData();
        if (!messageData.containsKey(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }

        messageRepository.delete(messageId);
    }
}
