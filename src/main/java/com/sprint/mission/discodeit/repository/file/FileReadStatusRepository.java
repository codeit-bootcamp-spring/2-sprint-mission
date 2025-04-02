package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path directory;
    private final Map<UUID, ReadStatus> readStatusMap;
    private final FileRepository<ReadStatus> fileRepository;

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory}") String fileDir, FileRepository<ReadStatus> fileRepository) {
        this.directory = Paths.get(System.getProperty("user.dir"), fileDir, "readstatuses");
        SerializationUtil.init(directory);
        this.fileRepository = fileRepository;
        this.readStatusMap = new ConcurrentHashMap<>();
        loadCacheFromFile();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        fileRepository.saveToFile(readStatus, directory);
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
        fileRepository.deleteFileById(id, directory);
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


    private void loadCacheFromFile() {
        List<ReadStatus> readStatuses = fileRepository.loadAllFromFile(directory);
        for (ReadStatus readStatus : readStatuses) {
            readStatusMap.put(readStatus.getId(), readStatus);
        }
    }
}
