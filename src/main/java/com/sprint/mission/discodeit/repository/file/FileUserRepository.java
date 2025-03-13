package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final String fileName = "user.ser";
    private final Map<UUID, User> userMap;
    private final FileDataManager fileDataManager;

    public FileUserRepository() {
        this.fileDataManager = new FileDataManager(fileName);
        this.userMap = loadUserList();
    }

    public void saveUserList() {
        fileDataManager.saveObjectToFile(userMap);
    }

    public Map<UUID, User> loadUserList() {
        Map<UUID, User> loadedData = fileDataManager.loadObjectFromFile();
        if (loadedData == null) {
            return new HashMap<>();
        }
        return loadedData;
    }

    @Override
    public User save(User user) {
        this.userMap.put(user.getId(), user);
        saveUserList();
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public User update(User user) {
        this.userMap.put(user.getId(), user);
        saveUserList();
        return user;
    }

    @Override
    public boolean delete(UUID userId) {
        boolean removed = userMap.remove(userId) != null;
        if (removed) {
            saveUserList();
        }
        return removed;
    }
}
