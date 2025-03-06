package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileUserRepository implements FileRepository<User>, UserRepository {
    private static volatile FileUserRepository instance;
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");
    private final Map<UUID, User> userMap;

    public static FileUserRepository getInstance() {
        if (instance == null) {
            synchronized (FileUserRepository.class) {
                if (instance == null) {
                    instance = new FileUserRepository();
                }
            }
        }
        return instance;
    }

    private FileUserRepository() {
        SerializationUtil.init(directory);
        userMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public User save(User user) {
        userMap.put(user.getId(), user);
        saveToFile(user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void deleteById(UUID userId) {
        userMap.remove(userId);
        deleteFileById(userId);
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
    public void deleteFileById(UUID userId) {
        Path filePath = directory.resolve(userId + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("유저 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }

    private void loadCacheFromFile() {
        List<User> users = SerializationUtil.reverseSerialization(directory);
        for (User user : users) {
            userMap.put(user.getId(), user);
        }
    }
}
