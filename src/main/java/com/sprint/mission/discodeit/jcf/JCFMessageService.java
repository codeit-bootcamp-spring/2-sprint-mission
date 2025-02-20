package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.application.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messages = new HashMap<>();

    @Override
    public MessageDto create(String context) {
        Message message = new Message(context);
        messages.put(message.getId(), message);

        return new MessageDto(message.getId(), message.getContext());
    }

    @Override
    public List<MessageDto> findById(UUID id) {
        return messages.entrySet()
                .stream()
                .filter(message -> message.getKey().equals(id))
                .map(message -> new MessageDto(message.getKey(), message.getValue().getContext()))
                .toList();
    }

    @Override
    public List<MessageDto> findAll() {
        return messages.entrySet()
                .stream()
                .map(message -> new MessageDto(message.getKey(), message.getValue().getContext()))
                .toList();
    }
}
