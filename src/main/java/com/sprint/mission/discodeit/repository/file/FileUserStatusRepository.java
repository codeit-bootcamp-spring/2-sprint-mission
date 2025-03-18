package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path storagePath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<UUID, UserStatus> storage = new ConcurrentHashMap<>();

    public FileUserStatusRepository(String filePath) {
        this.storagePath = Paths.get(filePath);
        loadFromFile();
    }

    private void loadFromFile() {
        if (Files.exists(storagePath)) {
            try {
                storage = objectMapper.readValue(Files.readAllBytes(storagePath),
                        new TypeReference<Map<UUID, UserStatus>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Failed to load user status data", e);
            }
        }
    }

    private void saveToFile() {
        try {
            Files.write(storagePath, objectMapper.writeValueAsBytes(storage));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user status data", e);
        }
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(storage.get(userId));
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        if (userStatus.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        storage.put(userStatus.getUserId(), userStatus);
        saveToFile();
        return userStatus;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        storage.remove(userId);
        saveToFile();
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return storage.containsKey(userId);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(storage.values());
    }
}
