package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.Comparator;

@Repository
@Primary
public class FileMessageRepository extends AbstractFileRepository<Map<UUID, Message>> implements MessageRepository {

    private Map<UUID, Message> data;

    public FileMessageRepository() {
        super("Message");
        this.data = loadData();
    }

    @Override
    protected Map<UUID, Message> getEmptyData() {
        return new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        saveData(data);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        saveData(data);
    }

    @Override
    public Optional<Message> findLatestMessageByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> keysToRemove = data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getId)
                .toList();
        keysToRemove.forEach(data::remove);
        saveData(data);
    }
}
