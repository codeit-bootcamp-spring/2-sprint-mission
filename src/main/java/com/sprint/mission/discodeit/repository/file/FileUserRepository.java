package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {
    private final Path directory;
    private final Map<UUID, User> userMap;
    private final FileRepository<User> fileRepository;

    public FileUserRepository(@Value("${discodeit.repository.file-directory}") String fileDir, FileRepository<User> fileRepository) {
        this.directory = Paths.get(System.getProperty("user.dir"), fileDir, "users");
        SerializationUtil.init(directory);
        this.fileRepository = fileRepository;
        userMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public User save(User user) {
        fileRepository.saveToFile(user, directory);
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
        fileRepository.deleteFileById(id, directory);
        userMap.remove(id);
    }

    private void loadCacheFromFile() {
        List<User> users = fileRepository.loadAllFromFile(directory);
        for (User user : users) {
            userMap.put(user.getId(), user);
        }
    }
}
