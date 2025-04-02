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

import static com.sprint.mission.discodeit.util.FileUtils.loadAndSave;
import static com.sprint.mission.discodeit.util.FileUtils.loadObjectsFromFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path userStatusPath;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory.user-status-path}") Path userStatusPath) {
        this.userStatusPath = userStatusPath;
    }

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
    public void delete(UUID id) {
        loadAndSave(userStatusPath, (Map<UUID, UserStatus> userStatuses) ->
                userStatuses.remove(id)
        );
    }
}
