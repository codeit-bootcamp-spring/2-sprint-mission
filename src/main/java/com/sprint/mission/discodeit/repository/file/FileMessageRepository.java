package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final String fileName = "message.ser";
    private final Map<UUID, Message> messageMap;

    public FileMessageRepository() {
        this.messageMap = loadMessageList();
    }

    public void saveMessageList() {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(messageMap);
        } catch (IOException e) {
            throw new RuntimeException("데이터를 저장하는데 실패했습니다.", e);
        }
    }

    public Map<UUID, Message> loadMessageList() {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object messageMap = ois.readObject();
            return (Map<UUID, Message>) messageMap;
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("데이터를 불러오는데 실패했습니다", e);
        }
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
