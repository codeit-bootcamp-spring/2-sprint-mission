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
import org.springframework.stereotype.Repository;

@Repository
public class FileUserRepository implements UserRepository {

    private static final Path DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "data",
            "users");

    public FileUserRepository() {
        init();
    }

    private void init() {
        try {
            Files.createDirectories(DIRECTORY_PATH);
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
        return DIRECTORY_PATH.resolve(userId + ".ser");
    }

    @Override
    public List<User> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY_PATH)) {
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
        return findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
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
