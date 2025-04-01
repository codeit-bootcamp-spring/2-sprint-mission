package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path DIRECTORY;

    public FileReadStatusRepository(
            @Value("${discodeit.repository.file-directory:file-data-map}") String fileDirectory
    ) {
        DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory,
                ReadStatus.class.getSimpleName());
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
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return FileUtil.findAll(DIRECTORY, ReadStatus.class).stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId)).toList();
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