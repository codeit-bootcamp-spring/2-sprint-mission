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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUserRepository implements UserRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"),
            "src/main/java/com/sprint/mission/discodeit/User");

    private boolean fileExists(String filename) {
        return Files.exists(directory.resolve(filename + ".ser"));
    }

    private void save(String userName, User user) {
        Path filePath = directory.resolve(userName + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User loadUserFile(String userName) {
        Path filePath = directory.resolve(userName + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패: " + e.getMessage(), e);
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
            throw new RuntimeException("파일 로드 실패");
        }
    }

    private void deleteUserFile(String userName) {
        Path filePath = directory.resolve(userName + ".ser");
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제 불가");
            }
        }
    }

    @Override
    public User findByName(String userName) {
        if (!fileExists(userName)) {
            throw new IllegalArgumentException("존재하지 않는 사용자명입니다.");
        }
        return loadUserFile(userName);
    }

    @Override
    public List<User> findAll() {
        return loadAllUsersFile();
    }

    @Override
    public List<User> findUpdatedUsers() {
        return loadAllUsersFile().stream()
                .filter(user -> user.getUpdatedAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void createUser(String userName, String nickName) {
        if (fileExists(userName)) {
            throw new RuntimeException("존재하는 사용자명입니다.");
        }
        User user = new User(userName, nickName);
        save(userName, user);
    }

    @Override
    public void updateUser(String oldUserName, String newUserName, String newNickName) {
        if (!fileExists(oldUserName)) {
            throw new IllegalArgumentException("존재하지 않는 사용자명입니다.");
        }
        User user = findByName(oldUserName);
        deleteUserFile(oldUserName);
        user.userUpdate(newUserName, newNickName);
        save(newUserName, user);
    }

    @Override
    public void deleteUser(String userName) {
        if (!fileExists(userName)) {
            throw new IllegalArgumentException("존재하지 않는 사용자명입니다.");
        }
        deleteUserFile(userName);
    }

}
