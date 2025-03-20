package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final Path DIRECTORY
            = Paths.get(System.getProperty("user.dir"), "file-data-map", ReadStatus.class.getSimpleName());

    public FileReadStatusRepository() {
        FileUtil.init(DIRECTORY);
    }

    @Override
    public void save(ReadStatus readStatus) {
        FileUtil.save(DIRECTORY, readStatus, readStatus.getId());
    }

    @Override
    public Optional<ReadStatus> find(UUID id) {
        return FileUtil.findById(DIRECTORY, id, ReadStatus.class);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return FileUtil.findAll(DIRECTORY, ReadStatus.class).stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<UUID> findAllUserByChannelId(UUID channelId) { // channelId를 가지고 있는 ReadStatus들의 userId모음
        return FileUtil.findAll(DIRECTORY, ReadStatus.class).stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId).toList();
    }

    @Override
    public List<UUID> findAllByChannelId(UUID channelId) { // channelId를 가지고 있는 ReadStatus들의 id모음
        return FileUtil.findAll(DIRECTORY, ReadStatus.class).stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(ReadStatus::getId).toList();
    }

    @Override
    public void delete(UUID id) {
        FileUtil.delete(DIRECTORY, id);
    }

    @Override
    public boolean existsById(UUID id) {
        return FileUtil.existsById(DIRECTORY, id);
    }
}