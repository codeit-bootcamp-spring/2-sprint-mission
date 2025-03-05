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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository, FileRepository<Message> {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");

    @Override
    public Message save(Message message) {
        saveToFile(message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return loadOneFromFileById(messageId);
    }

    @Override
    public List<Message> findAll() {
        return loadAllFromFile();
    }

    @Override
    public void deleteById(UUID messageId) {
        deleteFileById(messageId);
    }

    @Override
    public void saveToFile(Message message) {
        Path filePath = directory.resolve(message.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, message);
    }

    @Override
    public Optional<Message> loadOneFromFileById(UUID messageId) {
        return Optional.ofNullable(SerializationUtil.reverseOneSerialization(directory,messageId));
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
}
