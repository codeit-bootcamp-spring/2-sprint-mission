package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;


import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private static final String FILE_NAME = "message.ser";
    private Map<UUID, Message> data;

    public FileMessageService() {
        this.data = loadFromFile();
    }


    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        data.put(message.getId(), message);
        saveToFile();
        return message;
    }

    @Override
    public Message find(UUID messageId){
        Message message = data.get(messageId);
        return Optional.ofNullable(message)
                .orElseThrow(()-> new NoSuchElementException("Message with id" + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = find(messageId);
        message.update(newContent);
        saveToFile();
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (!data.containsKey(messageId)) {
            throw new NoSuchElementException("Message with id" + messageId + " not found");
        }
        data.remove(messageId);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Error saving messages data", e);
        }
    }

    private Map<UUID, Message> loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading messages data", e);
        }
    }
}
