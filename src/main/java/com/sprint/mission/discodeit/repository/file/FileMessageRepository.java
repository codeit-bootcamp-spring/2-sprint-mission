package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
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
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> messages = new ArrayList<>();
        for (Message message : messageMap.values()) {
            if (message.getChannelId().equals(channelId)) {
                messages.add(message);
            }
        }
        return messages;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageMap.get(messageId));
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messageMap.containsKey(messageId);
    }

    @Override
    public void deleteById(UUID messageId) {
        boolean removed = messageMap.remove(messageId) != null;
        if (removed) {
            saveMessageList();
        }
    }
}
