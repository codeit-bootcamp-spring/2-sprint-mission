package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileUserService implements UserService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserService() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName());
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

    private void serializeUser(User user, Path path) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("사용자 데이터를 저장하는 중 오류 발생: " + path, e);
        }
    }

    private User deserializeUser(Path path) {
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
    public User createUser(String userName, String email, String password) {
        User user = new User(userName, email, password);
        serializeUser(user, getFilePath(user.getId()));
        System.out.println("사용자가 생성되었습니다: \n" + user);
        return user;
    }

    @Override
    public User searchUser(UUID userId) {
        return deserializeUser(getFilePath(userId));
    }

    @Override
    public List<User> searchAllUsers() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> deserializeUser(path))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User updateAll(UUID userId, String userName, String email, String password) {
        existUser(userId);
        Path path = getFilePath(userId);
        User user = deserializeUser(path);
        user.updateAll(userName, email, password);
        serializeUser(user, path);
        return user;
    }

    @Override
    public User updateUserName(UUID userId, String userName) {
        existUser(userId);
        Path path = getFilePath(userId);
        User user = deserializeUser(path);
        user.updateUserName(userName);
        serializeUser(user, path);
        return user;
    }

    @Override
    public User updateEmail(UUID userId, String email) {
        existUser(userId);
        Path path = getFilePath(userId);
        User user = deserializeUser(path);
        user.updateEmail(email);
        serializeUser(user, path);
        return user;
    }

    @Override
    public User updatePassword(UUID userId, String password) {
        existUser(userId);
        Path path = getFilePath(userId);
        User user = deserializeUser(path);
        user.updatePassword(password);
        serializeUser(user, path);
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        existUser(userId);
        Path path = getFilePath(userId);
        try {
            Files.delete(path);
            System.out.println(userId + " 사용자 삭제 완료되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException("사용자 삭제 실패: " + userId, e);
        }
    }

    public boolean existUser(UUID userId) {
        Path path = getFilePath(userId);
        if (!Files.exists(path)) {
            throw new NoSuchElementException("사용자가 존재하지 않습니다: " + userId);
        }
        return true;
    }
}
