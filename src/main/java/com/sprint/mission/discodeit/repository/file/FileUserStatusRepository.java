package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserStatusRepository {
    private final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map",
            UserStatus.class.getSimpleName());
    private final String EXTENSION = ".ser";

    public FileUserStatusRepository() {
        FileUtil.init(DIRECTORY);
    }

    public UserStatus save(UserStatus userStatus) {
        return FileUtil.save(DIRECTORY, userStatus, userStatus.getId());
    }

    public Optional<UserStatus> findById(UUID id) {
        return FileUtil.findById(DIRECTORY, id);
    }

    public List<UserStatus> findAll() {
        return FileUtil.findAll(DIRECTORY);
    }

    public void deleteById(UUID id) {
        FileUtil.delete(DIRECTORY, id);
    }

    public boolean existsById(UUID id) {
        return FileUtil.existsById(DIRECTORY, id);
    }

    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream().filter(u -> u.getUserId().equals(userId)).findFirst();
    }

    public boolean existsByUserId(UUID userId) {
        return findAll().stream().anyMatch(u -> u.getUserId().equals(userId));
    }

}
