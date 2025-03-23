package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final String fileName = "userStatus.ser";
    private final Map<UUID, UserStatus> userStatusMap;
    private final FileDataManager fileDataManager;

    public FileUserStatusRepository() {
        this.fileDataManager = new FileDataManager(fileName);
        this.userStatusMap = loadUserStatusList();
    }

    private Map<UUID, UserStatus> loadUserStatusList() {
        Map<UUID, UserStatus> loadedData = fileDataManager.loadObjectFromFile();
        if (loadedData == null) {
            return new HashMap<>();
        }
        return loadedData;
    }

    private void saveUserStatusList() {
        fileDataManager.saveObjectToFile(userStatusMap);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        this.userStatusMap.put(userStatus.getUserId(), userStatus);
        saveUserStatusList();
        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(userStatusMap.get(userStatusId));
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        return userStatusMap.containsKey(userStatusId);
    }

    @Override
    public void deleteById(UUID userStatusId) {
        boolean removed = userStatusMap.remove(userStatusId) != null;
        if (removed) {
            saveUserStatusList();
        }
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(userStatusMap.get(userId));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return userStatusMap.containsKey(userId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        boolean removed = userStatusMap.remove(userId) != null;
        if (removed) {
            saveUserStatusList();
        }
    }
}
