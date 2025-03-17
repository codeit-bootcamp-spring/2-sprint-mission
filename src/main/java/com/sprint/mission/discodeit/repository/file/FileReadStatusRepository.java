package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path storagePath = Paths.get("read_status.json");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<UUID, ReadStatus> storage = new ConcurrentHashMap<>();

    public FileReadStatusRepository() {
        loadFromFile();
    }

    private void loadFromFile() {
        if (Files.exists(storagePath)) {
            try {
                storage = objectMapper.readValue(Files.readAllBytes(storagePath),
                        new TypeReference<Map<UUID, ReadStatus>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Failed to load read status data", e);
            }
        }
    }

    private void saveToFile() {
        try {
            Files.write(storagePath, objectMapper.writeValueAsBytes(storage));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read status data", e);
        }
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return storage.values().stream()
                .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        if (readStatus.getId() == null) {
            readStatus.setId(UUID.randomUUID());
        }
        storage.put(readStatus.getId(), readStatus);
        saveToFile();
        return readStatus;
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
        saveToFile();
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return storage.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UUID> findUserIdsByChannelId(UUID channelId) {
        return storage.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return storage.values().stream()
                .anyMatch(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId));
    }
}
