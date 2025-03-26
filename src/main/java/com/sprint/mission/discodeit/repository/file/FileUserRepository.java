package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
public class FileUserRepository implements UserRepository {

    private final Path directoryPath;

    public FileUserRepository(@Value("${discodeit.repository.file-directory}") String directoryPath) {
        this.directoryPath = Paths.get(System.getProperty("user.dir"), directoryPath, "users");
        init();
    }

    private void init() {
        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException("User 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public User save(User user) {
        Path filePath = getFilePath(user.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(user);
            return user;
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " User 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID userId) {
        return directoryPath.resolve(userId + ".ser");
    }

    @Override
    public List<User> findAll() {
        try (Stream<Path> paths = Files.list(directoryPath)) {
            return paths.map(this::readUserFromFile).toList();
        } catch (IOException e) {
            throw new RuntimeException("Users 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    private User readUserFromFile(Path filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(filePath.getFileName() + " User 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public User findById(UUID userId) {
        Path filePath = getFilePath(userId);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("User ID " + userId + "에 해당하는 파일을 찾을 수 없습니다.");
        }

        return readUserFromFile(filePath);
    }

    @Override
    public void delete(UUID userId) {
        Path filePath = getFilePath(userId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " User 삭제를 실패했습니다: " + e.getMessage());
        }
    }
}
