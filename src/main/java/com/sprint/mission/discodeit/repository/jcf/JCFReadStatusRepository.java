package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(value = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, ReadStatus> data;

  public JCFReadStatusRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    data.put(readStatus.getId(), readStatus);
    return readStatus;
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
  }

  @Override
  public void deleteByChannelId(UUID channelId) {
    List<UUID> keysToRemove = data.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .map(ReadStatus::getId)
        .toList();
    keysToRemove.forEach(data::remove);
  }
}
