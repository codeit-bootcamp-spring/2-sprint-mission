package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_NAME = "message.ser";
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message save(Message message) {
        data.put(message.getUuid(), message);
        saveToFile();
        return data.get(message.getUuid());
    }

    @Override
    public void delete(UUID messageId) {
        data.remove(messageId);
        saveToFile();
    }

    @Override
    public Message findByKey(UUID messageKey) {
        return data.get(messageKey);
    }

    @Override
    public List<Message> findAllByChannelKey(UUID channelKey) {
        return data.values().stream()
                .filter(m -> m.getChannelKey().equals(channelKey))
                .toList();
    }

    @Override
    public Message findByMessageId(int messageId) {
        return data.values().stream()
                .filter(m -> m.getMessageId() == messageId)
                .findFirst()
                .orElse(null);
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[Error] 사용자 데이터를 저장하는 중 문제가 발생했습니다.");
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry.getKey() instanceof UUID key && entry.getValue() instanceof Message value) {
                        data.put(key, value);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] 사용자 데이터를 불러오는 중 문제가 발생했습니다.");
        }
    }
}
