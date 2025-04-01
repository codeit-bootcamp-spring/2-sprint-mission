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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileMessageRepository implements MessageRepository {

    private final Path directoryPath;

    public FileMessageRepository(@Value("${discodeit.repository.file-directory}") String directoryPath) {
        this.directoryPath = Paths.get(System.getProperty("user.dir"), directoryPath, "messages");
        init();
    }

    private void init() {
        try {
            Files.createDirectories(directoryPath);
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

    private Path getFilePath(UUID messageId) {
        return directoryPath.resolve(messageId + ".ser");
    }

    @Override
    public List<Message> findAll() {
        try (Stream<Path> paths = Files.list(directoryPath)) {
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
        Path filePath = getFilePath(messageId);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("Message ID " + messageId + "에 해당하는 파일을 찾을 수 없습니다.");
        }

        return readUserFromFile(filePath);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        try (Stream<Path> paths = Files.list(directoryPath)) {
            return paths
                    .map(this::readUserFromFile)
                    .filter(message -> message.getChannelId().equals(channelId))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Messages 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public List<Message> findAllByAuthorId(UUID authorId) {
        try (Stream<Path> paths = Files.list(directoryPath)) {
            return paths
                    .map(this::readUserFromFile)
                    .filter(message -> message.getAuthorId().equals(authorId))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Messages 데이터 로드를 실패했습니다: " + e.getMessage());
        }
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
