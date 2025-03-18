package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileMessageRepository implements MessageRepository {

    private static final String FILE_PATH = "src/main/resources/messages.dat";
    private static Map<UUID, Message> messages = new ConcurrentHashMap<>();
    private final FileStorageManager fileStorageManager;

    @Autowired
    public FileMessageRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        messages = fileStorageManager.loadFile(FILE_PATH);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(FILE_PATH, messages);
    }

    @Override
    public void addMessage(Message message) {
        messages.put(message.getId(), message);
        fileStorageManager.saveFile(FILE_PATH, messages);
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
        fileStorageManager.saveFile(FILE_PATH, messages);
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messages.containsKey(messageId);
    }
}
