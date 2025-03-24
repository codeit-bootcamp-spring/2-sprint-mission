package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final String fileName = "readStatus.ser";
    private final Map<UUID, ReadStatus> readStatusMap;
    private final FileDataManager fileDataManager;

    public FileReadStatusRepository() {
        this.fileDataManager = new FileDataManager(fileName);
        this.readStatusMap = loadReadStatusList();
    }

    private Map<UUID, ReadStatus> loadReadStatusList() {
        Map<UUID, ReadStatus> loadedData = fileDataManager.loadObjectFromFile();
        if (loadedData == null) {
            return new HashMap<>();
        }
        return loadedData;
    }

    private void saveReadStatusList() {
        fileDataManager.saveObjectToFile(readStatusMap);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        this.readStatusMap.put(readStatus.getId(), readStatus);
        saveReadStatusList();
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusMap.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return readStatusMap.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        boolean removed = readStatusMap.remove(id) != null;
        if (removed) {
            saveReadStatusList();
        }
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }
}
