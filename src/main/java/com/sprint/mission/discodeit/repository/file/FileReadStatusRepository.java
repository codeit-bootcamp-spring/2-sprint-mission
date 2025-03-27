package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private final String READ_STATUS_FILE;
    private final Map<UUID, ReadStatus> readStatusData;
    private final SaveLoadHandler<ReadStatus> saveLoadHandler;

    public FileReadStatusRepository(@Value("${discodeit.repository.file.readStatus}") String fileName,SaveLoadHandler<ReadStatus> saveLoadHandler) {
        READ_STATUS_FILE = fileName;
        this.saveLoadHandler = saveLoadHandler;
        readStatusData = saveLoadHandler.loadData(READ_STATUS_FILE);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusData.put(readStatus.getId(), readStatus);
        saveLoadHandler.saveData(READ_STATUS_FILE, readStatusData);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusData.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return readStatusData.values().stream().toList();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusData.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusData.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public ReadStatus update(UUID id) {
        ReadStatus readStatusNullable = readStatusData.get(id);
        ReadStatus readStatus = Optional.ofNullable(readStatusNullable).orElseThrow(() -> new NoSuchElementException(("상태를 찾을 수 없습니다.")));
        readStatus.updateLastCheckedAt();
        saveLoadHandler.saveData(READ_STATUS_FILE, readStatusData);
        return readStatus;
    }

    @Override
    public void delete(UUID id) {
        readStatusData.remove(id);
        saveLoadHandler.saveData(READ_STATUS_FILE, readStatusData);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        readStatusData.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
    }
}
