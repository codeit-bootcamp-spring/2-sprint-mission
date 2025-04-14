package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(
    name = "discordit.repository.type",
    havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {

  private static final Map<UUID, ReadStatus> readStatusMap = new HashMap<>();

  @Override
  public void save() {
  }

  @Override
  public void addReadStatus(ReadStatus readStatus) {
    readStatusMap.put(readStatus.getId(), readStatus);
  }

  @Override
  public Optional<ReadStatus> findReadStatusById(UUID readStatusId) {
    return Optional.ofNullable(readStatusMap.get(readStatusId));
  }

  @Override
  public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
    return readStatusMap.values().stream()
        .filter(readStatus -> readStatus.getChannel().getId().equals(channelId))
        .filter(readStatus -> readStatus.getUser().getId().equals(userId))
        .findFirst();
  }

  @Override
  public List<ReadStatus> findAllReadStatus() {
    return new ArrayList<>(readStatusMap.values());
  }


  @Override
  public void deleteReadStatusById(UUID id) {
    readStatusMap.remove(id);
  }

  @Override
  public boolean existReadStatusById(UUID id) {
    return readStatusMap.containsKey(id);
  }
}
