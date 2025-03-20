package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@Primary
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final String FILE_PATH = "read_status_storage.ser";

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> storage = loadStorage();
        storage.put(readStatus.getId(), readStatus);
        saveStorage(storage);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Map<UUID, ReadStatus> storage = loadStorage();
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        Map<UUID, ReadStatus> storage = loadStorage();
        return storage.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> storage = loadStorage();
        return storage.values().stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        Map<UUID, ReadStatus> storage = loadStorage();
        return storage.values().stream()
                .filter(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(loadStorage().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, ReadStatus> storage = loadStorage();
        if (storage.containsKey(id)) {
            storage.remove(id);
            saveStorage(storage);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID, ReadStatus> storage = loadStorage();
        storage.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
        saveStorage(storage);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> storage = loadStorage();
        storage.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
        saveStorage(storage);
    }

    @Override
    public List<UUID> findUserIdsByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> storage = loadStorage();
        return storage.values().stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .distinct()
                .toList();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        Map<UUID, ReadStatus> storage = loadStorage();
        return storage.values().stream()
                .anyMatch(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId));
    }

    private Map<UUID, ReadStatus> loadStorage() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveStorage(Map<UUID, ReadStatus> storage) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
