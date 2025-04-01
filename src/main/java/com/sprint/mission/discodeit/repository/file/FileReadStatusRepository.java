package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository implements ReadStatusRepository {

    private static final String fileName = "readStatus.dat";
    private static Map<UUID, ReadStatus> readStatusMap = new ConcurrentHashMap<>();
    private final FileStorageManager fileStorageManager;

    public FileReadStatusRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        readStatusMap = fileStorageManager.loadFile(fileName);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(fileName, readStatusMap);
    }

    @Override
    public void addReadStatus(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        save();
    }

    @Override
    public ReadStatus findReadStatusById(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ReadStatus> findAllReadStatus() {
        return new ArrayList<>(readStatusMap.values());
    }

    @Override
    public void deleteReadStatusById(UUID id) {
        readStatusMap.remove(id);
        save();
    }

    @Override
    public boolean existReadStatusById(UUID id) {
        return readStatusMap.containsKey(id);
    }
}
