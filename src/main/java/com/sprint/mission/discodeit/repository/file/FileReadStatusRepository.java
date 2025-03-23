package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.READ_STATUS_TEST_FILE;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

public class FileReadStatusRepository implements ReadStatusRepository {
    private Path readStatusPath = READ_STATUS_TEST_FILE;

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> readStatuses = loadObjectsFromFile(readStatusPath);
        readStatuses.put(readStatus.getId(), readStatus);

        saveObjectsToFile(STORAGE_DIRECTORY, readStatusPath, readStatuses);

        return readStatus;
    }

    @Override
    public Optional<ReadStatus> find(UUID readStatusId) {
        Map<UUID, ReadStatus> readStatuses = loadObjectsFromFile(readStatusPath);

        return Optional.ofNullable(readStatuses.get(readStatusId));
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> readStatuses = loadObjectsFromFile(readStatusPath);

        return readStatuses.values()
                .stream()
                .toList();
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        Map<UUID, ReadStatus> readStatuses = loadObjectsFromFile(readStatusPath);
        return readStatuses.values()
                .stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public ReadStatus updateLastReadTime(UUID readStatusId) {
        Map<UUID, ReadStatus> readStatuses = loadObjectsFromFile(readStatusPath);
        ReadStatus readStatus = readStatuses.get(readStatusId);
        readStatus.updateLastReadTime();
        saveObjectsToFile(STORAGE_DIRECTORY, readStatusPath, readStatuses);

        return readStatus;
    }

    @Override
    public void delete(UUID readStatusId) {
        Map<UUID, ReadStatus> readStatuses = loadObjectsFromFile(readStatusPath);
        readStatuses.remove(readStatusId);
        saveObjectsToFile(STORAGE_DIRECTORY, readStatusPath, readStatuses);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> readStatuses = loadObjectsFromFile(readStatusPath);

        List<UUID> readStatusIdWithSameChannel = readStatuses.values()
                .stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(ReadStatus::getId)
                .toList();

        for (UUID readStatusId : readStatusIdWithSameChannel) {
            readStatuses.remove(readStatusId);
        }

        saveObjectsToFile(STORAGE_DIRECTORY, readStatusPath, readStatuses);
    }
}
