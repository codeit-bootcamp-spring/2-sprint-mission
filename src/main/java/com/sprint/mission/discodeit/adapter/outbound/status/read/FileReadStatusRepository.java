package com.sprint.mission.discodeit.adapter.outbound.status.read;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

  private final Path path = Paths.get(System.getProperty("user.dir"), "data", "ReadStatusList.ser");

  private Map<UUID, ReadStatus> readStatusList = new ConcurrentHashMap<>();
  private final FileRepositoryImpl<Map<UUID, ReadStatus>> fileRepository;

  public FileReadStatusRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);
    try {
      this.readStatusList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileReadStatusRepository init");
    }
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    readStatusList.put(readStatus.getReadStatusId(), readStatus);
    fileRepository.save(readStatusList);
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findById(UUID readStatusId) {
    return Optional.ofNullable(this.readStatusList.get(readStatusId));
  }

  @Override
  public ReadStatus findByUserId(UUID userID) {
    return readStatusList.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userID)).findFirst().orElse(null);
  }

  @Override
  public ReadStatus findByChannelId(UUID channelId) {
    return readStatusList.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId)).findFirst().orElse(null);
  }


  @Override
  public ReadStatus findByUserAndChannelId(UUID userId, UUID channelId) {
    return readStatusList.values().stream().filter(
        readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId()
            .equals(channelId)).findFirst().orElse(null);
  }


  @Override
  public List<ReadStatus> findAllByUserId(UUID userID) {
    return readStatusList.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userID))
        .toList();
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return readStatusList.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public void delete(UUID readStatusId) {
    readStatusList.remove(readStatusId);
    fileRepository.save(readStatusList);
  }
}
