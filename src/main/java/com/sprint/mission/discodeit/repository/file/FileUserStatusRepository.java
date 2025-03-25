package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(
        name = "discordit.repository.type",
        havingValue = "file",
        matchIfMissing = true)
public class FileUserStatusRepository implements UserStatusRepository {
    private static final String fileName = "userStatus.dat";
    private static Map<UUID, UserStatus> userStatusMap = new ConcurrentHashMap<>();
    private final FileStorageManager fileStorageManager;

    @Autowired
    public FileUserStatusRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        userStatusMap = fileStorageManager.loadFile(fileName);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(fileName, userStatusMap);
    }

    @Override
    public void addUserStatus(UserStatus userStatus) {
        userStatusMap.put(userStatus.getId(), userStatus);
        save();
    }

    @Override
    public UserStatus findUserStatusById(UUID userId) {
        return userStatusMap.get(userId);
    }

    @Override
    public List<UserStatus> findAllUserStatus() {
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public void deleteUserStatusById(UUID userId) {
        userStatusMap.remove(userId);
        save();
    }

    @Override
    public boolean existsUserStatusById(UUID userId) {
        return userStatusMap.containsKey(userId);
    }
}
