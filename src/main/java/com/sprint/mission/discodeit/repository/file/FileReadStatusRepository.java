package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.exception.MissingArgumentException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(value = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileReadStatusRepository extends
    AbstractFileRepository<Map<UUID, ReadStatus>> implements ReadStatusRepository {

  private Map<UUID, ReadStatus> data;

  public FileReadStatusRepository(
      @Value("${discodeit.repository.file-directory}") String directory) {
    super(directory, ReadStatus.class.getSimpleName() + ".ser");
    this.data = loadData();
  }

  @Override
  protected Map<UUID, ReadStatus> getEmptyData() {
    return new HashMap<>();
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    assertNotNull(readStatus);
    data.put(readStatus.getId(), readStatus);
    saveData(data);
    return readStatus;
  }

  private static void assertNotNull(ReadStatus readStatus) {
    if (readStatus == null) {
      throw new MissingArgumentException("저장할 readStatus를 제공받지 못했습니다.");
    }
  }

  @Override
  public Optional<ReadStatus> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
    return data.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId)
            && readStatus.getChannelId().equals(channelId))
        .findFirst();
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return data.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return data.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    data.remove(id);
    saveData(data);
  }

  @Override
  public void deleteByChannelId(UUID channelId) {
    List<UUID> keysToRemove = data.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .map(ReadStatus::getId)
        .toList();
    keysToRemove.forEach(data::remove);
    saveData(data);
  }
}
