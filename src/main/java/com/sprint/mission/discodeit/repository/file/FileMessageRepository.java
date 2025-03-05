package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages;

    public FileMessageRepository() {
        messages = loadFromFile("messages.ser");
    }

    @Override
    public void create(Message message) {
        messages.put(message.getId(), message);
        saveInFile(messages, "messages.ser");
    }

    @Override
    public void update(Message message) {
        messages.put(message.getId(), message);
        saveInFile(messages, "messages.ser");
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
        saveInFile(messages, "messages.ser");
    }

    @Override
    public Message find(UUID id) {
        return messages.getOrDefault(id, null);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    public static void saveInFile(Map<UUID, Message> messages, String fileName){
        try(FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(messages);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Map<UUID, Message> loadFromFile(String fileName){
        Map<UUID, Message> messages = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            messages = (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
