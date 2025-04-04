package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "readStatuss");

    private final FileSerializationUtil fileUtil;

    public FileReadStatusRepository(FileSerializationUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(ReadStatus readStatus) {
        fileUtil.writeObjectToFile(readStatus, FilePathUtil.getFilePath(directory, readStatus.getId()));
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return fileUtil.readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public Optional<List<ReadStatus>> findAll() {
        try {
            List<ReadStatus> readStatuses = Files.list(directory)
                    .map(fileUtil::<ReadStatus>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return Optional.of(readStatuses);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<ReadStatus>> findAllByUserId(UUID userId) {
        try {
            List<ReadStatus> readStatuses = Files.list(directory)
                    .map(fileUtil::<ReadStatus>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(readStatus -> readStatus.getUserId().equals(userId))
                    .toList();
            return Optional.of(readStatuses);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(ReadStatus readStatus) {
        save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        try {
            return Files.list(directory)
                    .map(fileUtil::<ReadStatus>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .anyMatch(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId));
        } catch (IOException e) {
            return false;
        }
    }
}
