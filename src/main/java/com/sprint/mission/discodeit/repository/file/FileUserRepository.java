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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUserRepository implements UserRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"),
            "src/main/java/com/sprint/mission/discodeit/data/User");

    public FileUserRepository() {
        init(directory);
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean userExists(String userName) {
        return !Files.exists(directory.resolve(userName + ".ser"));
    }

    private void save(String userName, User user) {
        Path filePath = directory.resolve(userName + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("유저 저장 실패" + e.getMessage());
        }
    }

    private List<User> loadAllUsersFile() {
        if (!Files.exists(directory)) {
            throw new IllegalArgumentException("디렉토리가 생성되어 있지 않음.");
        }
        try (Stream<Path> paths = Files.list(directory)) {
            return paths.map(path -> {
                try (
                        FileInputStream fis = new FileInputStream(path.toFile());
                        ObjectInputStream ois = new ObjectInputStream(fis)
                ) {
                    return (User) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("파일 로드 실패: " + e.getMessage(), e);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("파일 로드 실패" + e.getMessage());
        }
    }

    private void deleteUserFile(String userName) {
        Path filePath = directory.resolve(userName + ".ser");
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제 불가" + e.getMessage());
            }
        }
    }

    @Override
    public User findByName(String userName) {
        Path filePath = directory.resolve(userName + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("파일 로드 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> findAll() {
        return Optional.ofNullable(loadAllUsersFile())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("데이터가 존재하지 않습니다."));
    }

    @Override
    public List<User> findUpdatedUsers() {
        return loadAllUsersFile().stream()
                .filter(user -> user.getUpdatedAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void createUser(String userName, String nickName) {
        User user = new User(userName, nickName);
        save(userName, user);
    }

    @Override
    public void updateUser(String oldUserName, String newUserName, String newNickName) {
        User user = findByName(oldUserName);
        deleteUserFile(oldUserName);
        user.userUpdate(newUserName, newNickName);
        save(newUserName, user);
    }

    @Override
    public void deleteUser(String userName) {
        deleteUserFile(userName);
    }

}
