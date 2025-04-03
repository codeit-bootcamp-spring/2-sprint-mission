package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {

  private final Map<UUID, UserStatus> userStatuses = new HashMap<>();

  @Override
  public UserStatus save(UserStatus userStatus) {
    userStatuses.put(userStatus.getId(), userStatus);

    return userStatus;
  }

  @Override
  public Optional<UserStatus> findByUserStatusId(UUID id) {
    return Optional.ofNullable(userStatuses.get(id));
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return userStatuses.values()
        .stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatuses.values()
        .stream()
        .toList();
  }

  @Override
  public void delete(UUID id) {
    userStatuses.remove(id);
  }
}
