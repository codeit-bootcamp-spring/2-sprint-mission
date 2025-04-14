package com.sprint.mission.discodeit.adapter.outbound.status.read;

import static com.sprint.mission.discodeit.exception.status.read.ReadStatusErrors.nullPointReadStatusIdError;

import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.port.ReadStatusRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, ReadStatus> readStatusList = new ConcurrentHashMap<>();

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    readStatusList.put(readStatus.getId(), readStatus);
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findById(UUID readStatusId) {
    return Optional.ofNullable(this.readStatusList.get(readStatusId));
  }

  @Override
  public ReadStatus findByUserId(UUID userId) {
    return readStatusList.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId)).findFirst().orElse(null);
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
  public boolean existsId(UUID readStatusId) {
    if (readStatusId == null) {
      nullPointReadStatusIdError();
    }
    return readStatusList.containsKey(readStatusId);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (readStatusId == null) {
      nullPointReadStatusIdError();
    }
    readStatusList.remove(readStatusId);
  }
}
