package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public Message createMessage(MessageDTO messageDTO) {
        Message message = new Message(messageDTO.senderId(), messageDTO.channelId(), messageDTO.content());
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메세지입니다."));
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        Message message = getMessage(id);
        message.updateContent(newContent);
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID id) {
        getMessage(id);
        messageRepository.delete(id);
    }
}
