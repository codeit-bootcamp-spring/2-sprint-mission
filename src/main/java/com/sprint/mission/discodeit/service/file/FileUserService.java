package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUserService implements UserService {
    private static final Path DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "data",
            "users"); // 현재 디렉토리/data/users 경로

    private static volatile FileUserService instance = null;

    private FileUserService() {
        init();
    }

    private void init() {
        if (!Files.exists(DIRECTORY_PATH)) {
            try {
                Files.createDirectories(DIRECTORY_PATH);
            } catch (IOException e) {
                throw new RuntimeException("User 디렉토리 생성을 실패했습니다: " + e.getMessage());
            }
        }
    }

    public static FileUserService getInstance() {
        if (instance == null) {
            synchronized (FileUserService.class) {
                if (instance == null) {
                    instance = new FileUserService();
                }
            }
        }

        return instance;
    }

    private void save(User user) {
        Path filePath = getFilePath(user.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(filePath.getFileName() + " User 저장을 실패했습니다: " + e.getMessage());
        }
    }

    private Path getFilePath(UUID userId) {
        return DIRECTORY_PATH.resolve(userId + ".ser");
    }

    @Override
    public User create(String username, String email, String password) {
        boolean isExistUser = findAll().stream().anyMatch(user -> user.getEmail().equals(email)); // 동일한 이메일 == 같은 유저

        if (isExistUser) {
            throw new RuntimeException(email + " 이메일은 이미 가입되었습니다.");
        }

        User newUser = new User(username, email, password);
        save(newUser);

        return newUser;
    }

    @Override
    public User findById(UUID userId) {
        return findAll().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(userId + " 유저를 찾을 수 없습니다."));
    }

    @Override
    public List<User> findAll() {
        try (Stream<Path> files = Files.list(DIRECTORY_PATH)) {
            return files.map(file -> {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.toFile()))) {
                    return (User) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(file.getFileName() + " User 로드를 실패했습니다: " + e.getMessage());
                }
            }).toList();
        } catch (IOException e) {
            throw new RuntimeException("Users 데이터 로드를 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = findById(userId);
        user.update(newUsername, newEmail, newPassword);
        save(user);

        return user;
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
