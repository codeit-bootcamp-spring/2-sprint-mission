package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.application.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.infra.MessageRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages = new LinkedHashMap<>();

    @Override
    public MessageDto save(String context) {
        Message message = new Message(context);
        messages.put(message.getId(), message);

        return new MessageDto(message.getId(), message.getContext());
    }

    @Override
    public MessageDto findById(UUID id) {
        Message message = messages.get(id);
        if (message == null) {
            throw new IllegalArgumentException("[ERROR] 해당 메세지가 없습니다");
        }

        return new MessageDto(message.getId(), message.getContext());
    }

    @Override
    public List<MessageDto> findAll() {
        return messages.entrySet()
                .stream()
                .map(message -> new MessageDto(message.getKey(), message.getValue().getContext()))
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        messages.get(id)
                .updateContext(context);
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
