package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constant.FilePath.USER_STATUS_TEST_FILE;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

public class FileUserStatusRepository implements UserStatusRepository {

    private Path userStatusPath = USER_STATUS_TEST_FILE;

    @Override
    public UserStatus save(UserStatus userStatus) {
        Map<UUID, UserStatus> userStatuses = loadObjectsFromFile(userStatusPath);
        userStatuses.put(userStatus.getId(), userStatus);

        saveObjectsToFile(STORAGE_DIRECTORY, userStatusPath, userStatuses);

        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Map<UUID, UserStatus> userStatuses = loadObjectsFromFile(userStatusPath);
        return Optional.ofNullable(userStatuses.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        Map<UUID, UserStatus> userStatuses = loadObjectsFromFile(userStatusPath);

        return userStatuses.values()
                .stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        Map<UUID, UserStatus> userStatuses = loadObjectsFromFile(userStatusPath);
        return userStatuses.values()
                .stream()
                .toList();
    }

    @Override
    public UserStatus update(UUID userStatusId) {
        Map<UUID, UserStatus> userStatuses = loadObjectsFromFile(userStatusPath);
        UserStatus userStatus = userStatuses.get(userStatusId);
        userStatus.updateLastLoginAt();
        saveObjectsToFile(STORAGE_DIRECTORY, userStatusPath, userStatuses);

        return userStatus;
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, UserStatus> userStatuses = loadObjectsFromFile(userStatusPath);
        userStatuses.remove(id);
        saveObjectsToFile(STORAGE_DIRECTORY, userStatusPath, userStatuses);
    }

    @Override
    public void deleteByUserId(UUID id) {

    }
}
