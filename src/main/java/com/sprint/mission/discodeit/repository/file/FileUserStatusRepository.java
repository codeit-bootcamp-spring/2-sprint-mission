package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.SER_EXTENSION;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {

    @Value("${discodeit.repository.file-directory.user-status-path}")
    private Path userStatusPath = STORAGE_DIRECTORY.resolve("userStatus" + SER_EXTENSION);

    @Override
    public UserStatus save(UserStatus userStatus) {
        loadAndSave(userStatusPath, (Map<UUID, UserStatus> userStatuses) ->
                userStatuses.put(userStatus.getId(), userStatus)
        );

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
        return loadAndSave(userStatusPath, (Map<UUID, UserStatus> userStatuses) -> {
            UserStatus userStatus = userStatuses.get(userStatusId);
            userStatus.updateLastLoginAt();
            return userStatus;
        });
    }

    @Override
    public void delete(UUID id) {
        loadAndSaveConsumer(userStatusPath, (Map<UUID, UserStatus> userStatuses) ->
                userStatuses.remove(id)
        );
    }

    @Override
    public void deleteByUserId(UUID userId) {
        loadAndSaveConsumer(userStatusPath, (Map<UUID, UserStatus> userStatuses) ->
                userStatuses.values()
                        .removeIf(userStatus -> userStatus.getUserId().equals(userId))
        );
    }
}
