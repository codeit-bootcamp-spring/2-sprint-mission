package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static volatile FileMessageRepository instance;

    private final Path path;
    private Map<UUID, Message> messages;

    private FileMessageRepository(Path path) {
        messages = new HashMap<>();
        this.path = path;
        init(path.getParent());
        loadMessageData();
    }

    public static FileMessageRepository getInstance(Path path) {
        if (instance == null) {
            synchronized (FileMessageRepository.class) {
                if (instance == null) {
                    instance = new FileMessageRepository(path);
                }
            }
        }
        return instance;
    }

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        saveMessageData();
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
        saveMessageData();
    }

    @Override
    public boolean exists(UUID messageId) {
        return messages.containsKey(messageId);
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 안 됨");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadMessageData() {
        if (Files.exists(path)) {
            try (InputStream is = Files.newInputStream(path);
                 ObjectInputStream ois = new ObjectInputStream(is)) {
                Object messageDataObject = ois.readObject();

                if (messageDataObject instanceof Map) {
                    messages = (Map<UUID, Message>) messageDataObject;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("메세지 데이터 로드 실패");
            }
        }
    }

    private void saveMessageData() {
        try (OutputStream os = Files.newOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("메세지 데이터 저장 실패");
        }
    }
}
