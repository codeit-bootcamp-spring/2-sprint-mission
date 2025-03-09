package com.sprint.mission.discodeit.jcf.serviceimpl;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class JCFMessageServiceImplement implements MessageService {
    private final MessageRepository messageRepository;
    private final ValidationService validationService;

    public JCFMessageServiceImplement(
            MessageRepository messageRepository,
            ValidationService validationService) {
        this.messageRepository = messageRepository;
        this.validationService = validationService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        validationService.validateString(content, "메시지 내용");
        validationService.validateUUID(channelId, "채널 ID");
        validationService.validateUUID(authorId, "사용자 ID");
        
        // 사용자가 채널에 속해 있는지 확인
        validationService.validateUserInChannel(channelId, authorId);

        // 메시지 생성 및 저장
        Message message = new Message(channelId, authorId, content);
        messageRepository.register(message);
        return message;
    }

    @Override
    public Message findByMessage(UUID messageId) {
        validationService.validateUUID(messageId, "메시지 ID");
        
        return messageRepository.findById(messageId).orElse(null);
    }

    @Override
    public List<Message> findAllMessage() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateMessage(UUID messageId, String newContent) {
        validationService.validateUUID(messageId, "메시지 ID");
        validationService.validateString(newContent, "새 메시지 내용");
        
        Message message = findByMessage(messageId);
        if (message == null) {
            return null;
        }
        
        message.updateMessage(newContent);
        return message;
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        validationService.validateUUID(messageId, "메시지 ID");
        
        return messageRepository.deleteMessage(messageId);
    }
}