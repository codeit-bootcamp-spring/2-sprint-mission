package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private static final String FILE_PATH = "src/main/resources/readStatus.dat";
    private static Map<UUID, ReadStatus> readStatusMap = new ConcurrentHashMap<>();
    private final FileStorageManager fileStorageManager;

    public FileReadStatusRepository(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
        readStatusMap = fileStorageManager.loadFile(FILE_PATH);
    }

    @Override
    public void save() {
        fileStorageManager.saveFile(FILE_PATH, readStatusMap);
    }

    @Override
    public void addReadStatus(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getChannelId(), readStatus);
        save();
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        readStatusMap.get(channelId).addUser(userId);
        save();
    }

    @Override
    public ReadStatus findReadStatusById(UUID id) {
        return readStatusMap.get(id);
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
