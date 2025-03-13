package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.FileStorageManager;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {

    private static final String FILE_PATH = "src/main/resources/users.dat";
    private static Map<UUID, User> users = new HashMap<>();
    private final FileStorageManager fileStorageManager;
    
    public FileUserRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        users = fileStorageManager.loadFile(FILE_PATH);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(FILE_PATH, users);
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
        fileStorageManager.saveFile(FILE_PATH, users);
    }

    @Override
    public User findUserById(UUID userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findUsersByIds(Set<UUID> userIds) {
        return users.values().stream()
                .filter(user -> userIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUserAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(UUID userId) {
        users.remove(userId);
        fileStorageManager.saveFile(FILE_PATH, users);
    }

    @Override
    public boolean existsById(UUID userId) {
        return users.containsKey(userId);
    }
}
