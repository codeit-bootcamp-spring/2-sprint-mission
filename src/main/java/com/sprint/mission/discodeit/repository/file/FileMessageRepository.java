package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileMessageRepository implements MessageRepository {

    private static final String fileName = "messages.dat";
    private static Map<UUID, Message> messages = new ConcurrentHashMap<>();
    private final FileStorageManager fileStorageManager;

    @Autowired
    public FileMessageRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        messages = fileStorageManager.loadFile(fileName);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(fileName, messages);
    }

    @Override
    public void addMessage(Message message) {
        messages.put(message.getId(), message);
        save();
    }

    @Override
    public Message findMessageById(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public List<Message> findMessageAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void deleteMessageById(UUID messageId) {
        messages.remove(messageId);
        save();
    }

    @Override
    public void deleteMessageByChannelId(UUID channelId) {
        messages.values().removeIf(message -> message.getChannelId().equals(channelId));
        save();
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messages.containsKey(messageId);
    }

    @Override
    public Message findLatestMessageByChannelId(UUID channelId) {
        return messages.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt))
                .orElse(null);
    }
}
