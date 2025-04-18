package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final Map<UUID, ReadStatus> data;

  public JCFReadStatusRepository() {
    this.data = new HashMap<>();
  }

  public ReadStatus save(ReadStatusCreateRequest request) {
    ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(),
        request.lastReadAt());
    this.data.put(readStatus.getId(), readStatus);

    return readStatus;
  }

  public ReadStatus findById(UUID id) {
    return Optional.ofNullable(data.get(id))
        .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
  }

  public List<ReadStatus> findAll() {
    return this.data.values().stream().toList();
  }

  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return data.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId)).toList();
  }

  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = data.get(readStatusId);
    readStatus.update(request.newLastReadAt());

    return readStatus;
  }

  public void delete(UUID readStatusID) {
    data.remove(readStatusID);
  }
}
