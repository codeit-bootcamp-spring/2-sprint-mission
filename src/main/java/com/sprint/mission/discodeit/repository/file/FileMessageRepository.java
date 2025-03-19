package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileMessageRepository implements MessageRepository, FileRepository<Message> {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");
    private final Map<UUID, Message> messageMap;

    public FileMessageRepository() {
        SerializationUtil.init(directory);
        messageMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public Message save(Message message) {
        saveToFile(message);
        messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageMap.get(messageId));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageMap.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Optional<Instant> findLatestMessageTimeByChannelId(UUID channelId) {
        Optional<Instant> latestMessageTime = messageMap.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getUpdatedAt))
                .map(Message::getUpdatedAt);
        return latestMessageTime;
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<Message> messages = findAllByChannelId(channelId);
        messages.forEach(message -> messageMap.remove(message.getId()));
    }

    @Override
    public void deleteById(UUID messageId) {
        deleteFileById(messageId);
        messageMap.remove(messageId);
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
