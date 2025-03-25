package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.jboss.logging.Messages;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {
    private final HashMap<UUID, Message> messages = new HashMap<>();

    @Override
    public void save(Message message) {
        messages.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public Optional<List<Message>> findAll() {
        return Optional.of(new ArrayList<>(messages.values()));
    }

    @Override
    public Optional<List<Message>> findByChannelId(UUID channelId) {
        return Optional.of(new ArrayList<>(messages.values().stream().filter(message -> message.getChannelId().equals(channelId)).collect(Collectors.toList())));
    }

    @Override
    public void update(Message message) {
        save(message);
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }

    @Override
    public void deleteChannelById(UUID id) {
        findAll()
            .ifPresent( messages -> messages.removeIf( message -> message.getChannelId().equals(id)));
    }

}
