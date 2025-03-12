package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileMessageRepository implements MessageRepository, FileRepository<Message> {
    private static volatile FileMessageRepository instance;
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");
    private final Map<UUID, Message> messageMap;


    public static FileMessageRepository getInstance() {
        if (instance == null) {
            synchronized (FileMessageRepository.class) {
                if (instance == null) {
                    instance = new FileMessageRepository();
                }
            }
        }
        return instance;
    }

    private FileMessageRepository() {
        SerializationUtil.init(directory);
        messageMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public Message save(Message message) {
        messageMap.put(message.getId(), message);
        saveToFile(message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageMap.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public void deleteById(UUID messageId) {
        messageMap.remove(messageId);
        deleteFileById(messageId);
    }

    @Override
    public void saveToFile(Message message) {
        Path filePath = directory.resolve(message.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, message);
    }


    @Override
    public List<Message> loadAllFromFile() {
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFileById(UUID messageId) {
        Path filePath = directory.resolve(messageId + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("메시지 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }

    private void loadCacheFromFile() {
        List<Message> messages = loadAllFromFile();
        for (Message message : messages) {
            messageMap.put(message.getId(), message);
        }
    }
}
