package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final String READ_STATUS_FILE = "read_status.ser";
    private final Map<UUID, ReadStatus> readStatusData;
    private final SaveLoadHandler<ReadStatus> saveLoadHandler;

    public FileReadStatusRepository() {
        saveLoadHandler = new SaveLoadHandler<>(READ_STATUS_FILE);
        readStatusData = saveLoadHandler.loadData();
    }

    @Override
    public ReadStatus save() {

        saveLoadHandler.saveData(readStatusData);
        return null;
    }

    @Override
    public ReadStatus find(UUID id) {
        return null;
    }

    @Override
    public List<ReadStatus> findAll() {
        return List.of();
    }

    @Override
    public ReadStatus update(ReadStatus readStatus) {

        saveLoadHandler.saveData(readStatusData);
        return null;
    }

    @Override
    public void delete() {

        saveLoadHandler.saveData(readStatusData);
    }
}
