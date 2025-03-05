package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final String filePath = "messages.dat";
    private Map<UUID, Message> messages = new HashMap<>();

    public FileMessageService() {
        loadFromFile();
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        messages.put(message.getId(), message);
        saveToFile();
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messages.get(messageId);
        if (message != null) {
            message.updateContent(newContent);
            saveToFile();
        }
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                messages = (Map<UUID, Message>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
