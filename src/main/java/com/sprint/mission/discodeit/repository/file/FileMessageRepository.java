package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "file", matchIfMissing = true)
public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "messages.ser";

    @Override
    public Message save(Message message) {
        Map<UUID, Message> messages = loadMessages();
        messages.put(message.getId(), message);
        saveMessages(messages);
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        Map<UUID, Message> messages = loadMessages();
        return messages.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(loadMessages().values());
    }

    @Override
    public Message update(Message message) {
        return save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Map<UUID, Message> messages = loadMessages();
        messages.remove(messageId);
        saveMessages(messages);
    }

    @Override
    public boolean exists(UUID messageId) {
        Map<UUID, Message> messages = loadMessages();
        return messages.containsKey(messageId);
    }

    @Override
    public Instant findLatestMessageTimeByChannelId(UUID channelId) {
        return loadMessages().values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {

    }

    private Map<UUID, Message> loadMessages() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("file not found. Creating new one.");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("err. Returning new one.");
            return new HashMap<>();
        }
    }

    private void saveMessages(Map<UUID, Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
            System.out.println("messages saved.");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
