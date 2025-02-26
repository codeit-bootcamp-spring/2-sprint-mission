package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.repository.message.MessageRepository;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public JCFMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void create(Message message, UUID userId, UUID channelId) {

    }

    @Override
    public Message readById(UUID messageId){
        return messageRepository.findByMessageId(messageId);
    }

    @Override
    public List<Message> readAll(){
        return messageRepository.findAll();
    }

    @Override
    public Message remove(UUID messageId) {
        return messageRepository.delete(messageId);
    }
}
