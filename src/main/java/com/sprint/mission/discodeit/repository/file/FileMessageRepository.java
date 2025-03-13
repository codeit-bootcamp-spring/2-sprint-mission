package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class FileMessageRepository implements MessageRepository {

    private static final Path DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "data",
            "messages");

    public FileMessageRepository() {
        init();
    }

    private void init() {
        try {
            Files.createDirectories(DIRECTORY_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Message 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public Message save(Message message) {
        Path filePath = getFilePath(message.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(message);
            return message;
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " Message 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID userId) {
        return DIRECTORY_PATH.resolve(userId + ".ser");
    }

    @Override
    public List<Message> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY_PATH)) {
            return paths.map(this::readUserFromFile).toList();
        } catch (IOException e) {
            throw new RuntimeException("Messages 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    private Message readUserFromFile(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(filePath.getFileName() + " Message 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public Message findById(UUID messageId) {
        return findAll().stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> findByAuthorId(UUID authorId) {
        return findAll().stream()
                .filter(message -> message.getAuthorId().equals(authorId))
                .toList();
    }

    @Override
    public void delete(UUID messageId) {
        Path filePath = getFilePath(messageId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " Message 삭제를 실패했습니다: " + e.getMessage());
        }
    }
}
