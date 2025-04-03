package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity._ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, _ReadStatus> data;

  public JCFReadStatusRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public _ReadStatus save(_ReadStatus readStatus) {
    this.data.put(readStatus.getId(), readStatus);
    return readStatus;
  }

  @Override
  public Optional<_ReadStatus> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<_ReadStatus> findAllByUserId(UUID userId) {
    return this.data.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<_ReadStatus> findAllBygetChannelId(UUID getChannelId) {
    return this.data.values().stream()
        .filter(readStatus -> readStatus.getGetChannelId().equals(getChannelId))
        .toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    this.data.remove(id);
  }

  @Override
  public void deleteAllBygetChannelId(UUID getChannelId) {
    this.findAllBygetChannelId(getChannelId)
        .forEach(readStatus -> this.deleteById(readStatus.getId()));
  }
}
