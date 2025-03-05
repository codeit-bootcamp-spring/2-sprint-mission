package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public FileMessageService() {
        this.messageRepository = new FileMessageRepository();
    }

    @Override
    public Message createMessage(UUID channelId, UUID userId, String content) {
        Message message = new Message(channelId, userId, content);
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        Message message = messageRepository.findById(id);
        if (message == null) {
            throw new IllegalArgumentException("Message not found!");
        }
        return message;
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        Message message = getMessage(id); // 예외 처리된 메서드 활용
        message.updateContent(newContent);
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID id) {
        getMessage(id); // 존재 여부 체크
        messageRepository.delete(id);
    }
}
