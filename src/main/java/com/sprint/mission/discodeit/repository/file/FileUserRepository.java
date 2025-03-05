package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserRepository implements FileRepository<User>, UserRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");

    @Override
    public User save(User user) {
        saveToFile(user);
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return loadOneFromFileById(userId);
    }

    @Override
    public List<User> findAll() {
        return loadAllFromFile();
    }

    @Override
    public void deleteById(UUID userId) {
        deleteFileById(userId);
    }

    @Override
    public void saveToFile(User user) {
        Path filePath = directory.resolve(user.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, user);
    }

    @Override
    public User loadOneFromFileById(UUID userId) {
        return SerializationUtil.reverseOneSerialization(directory,userId);
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
}
