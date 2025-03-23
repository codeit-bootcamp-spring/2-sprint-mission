package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
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
    public Optional<User> findByUserName(String userName) {
        return userMap.values().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userMap.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        boolean removed = this.userMap.remove(id) != null;
        if (removed) {
            saveUserList();
        }
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userMap.values().stream()
                .anyMatch(user -> user.getUserName().equals(userName));
    }

    @Override
    public boolean existsByUserEmail(String userEmail) {
        return userMap.values().stream()
                .anyMatch(user -> user.getUserEmail().equals(userEmail));
    }
}
