package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Repository;

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
public class FileReadStatusRepository implements ReadStatusRepository, FileRepository<ReadStatus> {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "readstatuses");
    private final Map<UUID, ReadStatus> readStatusMap;

    public FileReadStatusRepository() {
        SerializationUtil.init(directory);
        this.readStatusMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        saveToFile(readStatus);
        readStatusMap.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusMap.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAll() {
        return readStatusMap.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        deleteFileById(id);
        readStatusMap.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = findAllByChannelId(channelId);
        readStatuses
                .forEach(readStatus -> deleteById(readStatus.getId()));
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId)
                        && readStatus.getChannelId().equals(channelId));
    }

    @Override
    public void saveToFile(ReadStatus readStatus) {
        Path filePath = directory.resolve(readStatus.getId() + ".ser");
        SerializationUtil.init(directory);
        SerializationUtil.serialization(filePath, readStatus);
    }

    @Override
    public List<ReadStatus> loadAllFromFile() {
        return SerializationUtil.reverseSerialization(directory);
    }

    @Override
    public void deleteFileById(UUID id) {
        Path filePath = directory.resolve(id + ".ser");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("읽음상태 파일 삭제 예외 발생 : " + e.getMessage());
        }
    }

    private void loadCacheFromFile() {
        List<ReadStatus> readStatuses = loadAllFromFile();
        for (ReadStatus readStatus : readStatuses) {
            readStatusMap.put(readStatus.getId(), readStatus);
        }
    }
}
