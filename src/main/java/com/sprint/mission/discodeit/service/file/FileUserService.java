package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserService implements UserService {

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        saveToFile(user);

        return user;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = loadOneFromFile(userId);

        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return loadAllFromFile().stream().toList();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User userNullable = loadOneFromFile(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.update(newUsername, newEmail, newPassword);

        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (loadOneFromFile(userId) == null) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        deleteFile(userId);
    }

    // ============================== 직렬화 관련 로직 ===================================
    @Override
    public void saveToFile(User user) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");
        Path filePath = directory.resolve(user.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, user);
    }

    @Override
    public User loadOneFromFile(UUID userId) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");
        return SerializationUtil.reverseOneSerialization(directory,userId);
    }

    @Override
    public List<User> loadAllFromFile() {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFile(UUID userId) {
        Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");
        Path filePath = directory.resolve(userId + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("유저 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }
}
