package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileReadStatusRepository implements ReadStatusRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileReadStatusRepository(RepositoryProperties properties) {
    this.DIRECTORY = Paths.get(properties.getReadStatus());
    FileUtil.init(DIRECTORY);
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    return FileUtil.saveToFile(DIRECTORY, readStatus, readStatus.getId());
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    return FileUtil.loadFromFile(DIRECTORY, id);
  }

  @Override
  public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
    return FileUtil.loadAllFiles(DIRECTORY, EXTENSION).stream()
        .filter(ReadStatus.class::isInstance)
        .map(ReadStatus.class::cast)
        .filter(ReadStatus -> ReadStatus.getUserId().equals(userId) && ReadStatus.getChannelId()
            .equals(channelId))
        .findFirst();
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return FileUtil.loadAllFiles(DIRECTORY, EXTENSION).stream()
        .filter(ReadStatus.class::isInstance)
        .map(ReadStatus.class::cast)
        .filter(ReadStatus -> ReadStatus.getUserId().equals(userId))
        .collect(Collectors.toList());
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return FileUtil.loadAllFiles(DIRECTORY, EXTENSION).stream()
        .filter(ReadStatus.class::isInstance)
        .map(ReadStatus.class::cast)
        .filter(ReadStatus -> ReadStatus.getChannelId().equals(channelId))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    FileUtil.deleteFile(DIRECTORY, id);
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
        .forEach(readStatus -> this.deleteById(readStatus.getId()));
  }


}
