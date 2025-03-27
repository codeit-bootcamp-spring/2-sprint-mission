package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileReadStatusRepository implements ReadStatusRepository {

    private final FileManager fileManager;

    @Override
    public void save(ReadStatus readStatus) {
        fileManager.writeToFile(SubDirectory.READ_STATUS, readStatus, readStatus.getId());
    }

    @Override
    public Optional<ReadStatus> find(UUID readStatusUUID) {
        return fileManager.readFromFileById(SubDirectory.READ_STATUS, readStatusUUID, ReadStatus.class);
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userUUID) {
        return fileManager.readFromFileAll(SubDirectory.READ_STATUS, ReadStatus.class).stream()
                .filter(readStatus -> readStatus.getUserId().equals(userUUID))
                .toList();
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelUUID) {
        return fileManager.readFromFileAll(SubDirectory.READ_STATUS, ReadStatus.class).stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelUUID))
                .toList();
    }

    @Override
    public void delete(UUID readStatusUUID) {
        fileManager.deleteFileById(SubDirectory.READ_STATUS, readStatusUUID);
    }
}
