package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final String fileName = "message.ser";
    private final Map<UUID, Message> messageMap;
    private final FileDataManager fileDataManager;

    public FileMessageRepository() {
        this.fileDataManager = new FileDataManager(fileName);
        this.messageMap = loadMessageList();
    }

    public void saveMessageList() {
        fileDataManager.saveObjectToFile(messageMap);
    }

    public Map<UUID, Message> loadMessageList() {
        Map<UUID, Message> loadedData = fileDataManager.loadObjectFromFile();
        if (loadedData == null) {
            return new HashMap<>();
        }
        return loadedData;
    }

    @Override
    public Message save(Message message) {
        this.messageMap.put(message.getId(), message);
        saveMessageList();
        return message;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageMap.get(messageId));
    }

    @Override
    public Message update(Message message) {
        this.messageMap.put(message.getId(), message);
        saveMessageList();
        return message;
    }

    @Override
    public boolean delete(UUID messageId) {
        boolean removed = messageMap.remove(messageId) != null;
        if (removed) {
            saveMessageList();
        }
        return removed;
    }
}
