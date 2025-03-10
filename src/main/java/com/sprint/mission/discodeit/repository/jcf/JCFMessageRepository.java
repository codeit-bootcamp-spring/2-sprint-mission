package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    JCFMessageRepository() {
        data = new HashMap<>();
    }

    @Override
    public Message save(Message message){
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public List<Message> findAll(){
        return this.data.values().stream().toList();
    }

    @Override
    public Message findById(UUID messageId){
        return Optional.ofNullable(data.get(messageId)).orElseThrow(()->new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public void delete(UUID messageId){
        this.data.remove(messageId);
    }
}
