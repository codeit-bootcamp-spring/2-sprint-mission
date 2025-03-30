package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.FileUtils.loadAndSave;
import static com.sprint.mission.discodeit.util.FileUtils.loadObjectsFromFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path readStatusPath;

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory.readStatus-path}") Path readStatusPath) {
        this.readStatusPath = readStatusPath;
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        loadAndSave(readStatusPath, (Map<UUID, ReadStatus> readStatuses) ->
                readStatuses.put(readStatus.getId(), readStatus));

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
    public void delete(UUID readStatusId) {
        loadAndSave(readStatusPath, (Map<UUID, ReadStatus> readStatuses) ->
                readStatuses.remove(readStatusId)
        );
    }
}
