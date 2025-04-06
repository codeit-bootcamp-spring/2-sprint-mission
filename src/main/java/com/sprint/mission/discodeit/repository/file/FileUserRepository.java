package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepository implements UserRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserRepository(
            @Value("${discodeit.repository.file-directory:data}") String fileDirectory
    ) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, User.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path getFilePath(UUID userId) {
        return DIRECTORY.resolve("user-" + userId + EXTENSION);
    }

    public void serialize(User user) {
        Path path = getFilePath(user.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("사용자 데이터를 저장하는 중 오류 발생: " + path, e);
        }
    }

    public User deserialize(Path path) {
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 파일을 읽는 중 오류 발생: " + path, e);
        }
    }

    @Override
    public User save(User user) {
        serialize(user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Path path = getFilePath(userId);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        return Optional.of(deserialize(path));
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> deserialize(path))
                    .filter(user -> user.getUsername() != null && user.getUsername().equals(userName))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("사용자 검색 중 오류 발생 (username: " + userName + ")", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> deserialize(path))
                    .filter(user -> user.getEmail().equals(email)) // email 비교
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("사용자 검색 중 오류 발생 (email: " + email + ")", e);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> deserialize(path))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("사용자 목록을 불러오는 중 오류 발생", e);
        }

    }

    @Override
    public boolean existsById(UUID userId) {
        return Files.exists(getFilePath(userId));
    }

    @Override
    public void deleteById(UUID userId) {
        Path path = getFilePath(userId);
        try {
            Files.deleteIfExists(path);
            System.out.println(userId + " 사용자 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("사용자 삭제 실패: " + userId, e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.findAll().stream()
                .anyMatch(user -> user.getUsername() != null && user.getUsername().equals(username));
    }
}
