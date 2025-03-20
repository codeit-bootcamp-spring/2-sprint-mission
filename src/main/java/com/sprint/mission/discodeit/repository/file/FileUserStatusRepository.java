package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.*;

@Repository
@Primary
public class FileUserStatusRepository implements UserStatusRepository {
    private static final String FILE_PATH = "user_status_storage.ser";

    @Override
    public UserStatus save(UserStatus userStatus) {
        Map<UUID, UserStatus> storage = loadStorage();
        storage.put(userStatus.getId(), userStatus);
        saveStorage(storage);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Map<UUID, UserStatus> storage = loadStorage();
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        Map<UUID, UserStatus> storage = loadStorage();
        return storage.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findOnlineUsers(Instant cutoffTime) {
        return List.of();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(loadStorage().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, UserStatus> storage = loadStorage();
        if (storage.containsKey(id)) {
            storage.remove(id);
            saveStorage(storage);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID, UserStatus> storage = loadStorage();
        storage.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
        saveStorage(storage);
    }

    private Map<UUID, UserStatus> loadStorage() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading storage. Returning empty map.");
            return new HashMap<>();
        }
    }

    private void saveStorage(Map<UUID, UserStatus> storage) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(storage);
            System.out.println("Storage saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving storage.");
        }
    }
}