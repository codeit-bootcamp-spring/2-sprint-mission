package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
public class JCFUserStatusRepository implements UserStatusRepository {

  private final Map<UUID, UserStatus> data;

  public JCFUserStatusRepository() {
    this.data = new HashMap<>();
  }

  public UserStatus save(UserStatus userStatus) {
    this.data.put(userStatus.getId(), userStatus);
    return userStatus;
  }

  public UserStatus update(UUID userStatusId, UserStatusUpdateRequest request) {
    UserStatus userStatus = data.get(userStatusId);
    userStatus.update(request.newLastActiveAt());

    return userStatus;
  }

  public List<UserStatus> findAll() {
    return this.data.values().stream().toList();
  }

  public UserStatus findById(UUID userStatusId) {
    return Optional.ofNullable(data.get(userStatusId))
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
  }

  public UserStatus findByUserId(UUID userId) {
    return data.values().stream()
        .filter(status -> status.getUserId().equals(userId))
        .findFirst()
        .orElseThrow(
            () -> new NoSuchElementException("UserStatus with userid " + userId + " not found"));
  }

  public void delete(UUID userStatusId) {
    data.remove(userStatusId);
  }
}
