package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.application.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    Map<UUID, Message> messages = new HashMap<>();

    @Override
    public MessageDto create(String context) {
        Message message = new Message(context);
        messages.put(message.getId(), message);

        return new MessageDto(message.getId(), message.getContext());
    }
}
