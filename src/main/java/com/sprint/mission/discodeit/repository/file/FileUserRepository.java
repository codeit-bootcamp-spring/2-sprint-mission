package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class FileUserRepository implements FileRepository<User>, UserRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");
    private final Map<UUID, User> userMap;

    public FileUserRepository() {
        SerializationUtil.init(directory);
        userMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public User save(User user) {
        saveToFile(user);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMap.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMap.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userMap.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public void deleteById(UUID id) {
        deleteFileById(id);
        userMap.remove(id);
    }

    @Override
    public void saveToFile(User user) {
        Path filePath = directory.resolve(user.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, user);
    }


    @Override
    public List<User> loadAllFromFile() {
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFileById(UUID id) {
        Path filePath = directory.resolve(id + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("유저 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }

    private void loadCacheFromFile() {
        List<User> users = loadAllFromFile();
        for (User user : users) {
            userMap.put(user.getId(), user);
        }
    }
}
