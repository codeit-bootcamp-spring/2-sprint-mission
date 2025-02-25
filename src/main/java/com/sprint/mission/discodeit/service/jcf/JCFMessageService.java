package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.application.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.infra.MessageRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository = new JCFMessageRepository();

    @Override
    public MessageDto create(String context) {
        Message message = messageRepository.save(
                new Message(context)
        );

        return new MessageDto(message.getId(), message.getContext());
    }

    @Override
    public MessageDto findById(UUID id) {
        Message message = messageRepository.findById(id);

        return new MessageDto(message.getId(), message.getContext());
    }

    @Override
    public List<MessageDto> findAll() {
        return messageRepository.findAll()
                .stream()
                .map(message -> new MessageDto(message.getId(), message.getContext()))
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        messageRepository.updateContext(id, context);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
