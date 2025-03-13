package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageRepository implements MessageRepository {
    private static final FileMessageRepository instance = new FileMessageRepository();
    private static final String FILE_PATH = "messages.ser";
    private final Map<UUID, Message> data;

    private FileMessageRepository() {
        this.data = loadData();
    }

    public static FileMessageRepository getInstance() {
        return instance;
    }

    private void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }
    }

    private Map<UUID, Message> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("메시지 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<Message> getMessageById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId));
    }

    @Override
    public List<Message> getAllMessagesByChannel(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMessage(UUID messageId) {
        data.remove(messageId);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("메시지 삭제 후 저장 중 오류 발생", e);
        }
    }
}
