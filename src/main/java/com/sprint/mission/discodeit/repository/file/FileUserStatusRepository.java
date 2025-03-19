package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
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
public class FileUserStatusRepository implements UserStatusRepository, FileRepository<UserStatus> {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "userstatuses");
    private final Map<UUID, UserStatus> userStatusMap;

    public FileUserStatusRepository() {
        SerializationUtil.init(directory);
        this.userStatusMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        saveToFile(userStatus);
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
        deleteFileById(id);
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

    @Override
    public void saveToFile(UserStatus userStatus) {
        Path filePath = directory.resolve(userStatus.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, userStatus);
    }

    @Override
    public List<UserStatus> loadAllFromFile() {
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFileById(UUID id) {
        Path filePath = directory.resolve(id + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("유저상태 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }

    private void loadCacheFromFile() {
        List<UserStatus> userStatuses = loadAllFromFile();
        for (UserStatus userStatus : userStatuses) {
            userStatusMap.put(userStatus.getId(), userStatus);
        }
    }
}
