package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {
    private final Path directory;
    private final Map<UUID, Message> messageMap;
    private final FileRepository<Message> fileRepository;

    public FileMessageRepository(@Value("${discodeit.repository.file-directory}") String fileDir, FileRepository<Message> fileRepository) {
        this.directory = Paths.get(System.getProperty("user.dir"), fileDir, "messages");
        SerializationUtil.init(directory);
        this.fileRepository = fileRepository;
        messageMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public Message save(Message message) {
        fileRepository.saveToFile(message,directory);
        messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageMap.get(id));
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
        messages.forEach(message -> {
            deleteById(message.getId());
            messageMap.remove(message.getId());
        });
    }

    @Override
    public void deleteById(UUID id) {
        fileRepository.deleteFileById(id, directory);
        messageMap.remove(id);
    }

    private void loadCacheFromFile() {
        List<Message> messages = fileRepository.loadAllFromFile(directory);
        for (Message message : messages) {
            messageMap.put(message.getId(), message);
        }
    }
}
