package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path directory;
    private final Map<UUID, UserStatus> userStatusMap;
    private final FileRepository<UserStatus> fileRepository;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory}") String fileDir, FileRepository<UserStatus> fileRepository) {
        this.directory = Paths.get(System.getProperty("user.dir"), fileDir, "userstatuses");
        SerializationUtil.init(directory);
        this.fileRepository = fileRepository;
        this.userStatusMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        fileRepository.saveToFile(userStatus, directory);
        userStatusMap.put(userStatus.getUserId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatusMap.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusMap.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusMap.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        fileRepository.deleteFileById(id, directory);
        userStatusMap.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findByUserId(userId)
                .ifPresent(userStatus -> deleteById(userStatus.getId()));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return findByUserId(userId).isPresent();
    }


    private void loadCacheFromFile() {
        List<UserStatus> userStatuses = fileRepository.loadAllFromFile(directory);
        for (UserStatus userStatus : userStatuses) {
            userStatusMap.put(userStatus.getId(), userStatus);
        }
    }
}
