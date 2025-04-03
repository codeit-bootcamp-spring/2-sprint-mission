package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {

  private final Map<UUID, UserStatus> data = new HashMap<>();

  @Override
  public UserStatus upsert(UserStatus userStatus) {
    data.put(userStatus.getUserId(), userStatus);
    return userStatus;
  }

  @Override
  public boolean isUserOnline(UUID userId) {
    UserStatus status = data.get(userId);
    return status != null && status.isCurrentOnline();
  }

  @Override
  public UserStatus findById(UUID id) {
    return data.get(id);
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    return this.findAllOnlineUsers().stream()
        .filter(us -> us.getUserId().equals(userId))
        .findFirst()
        .orElse(null);
  }

  @Override
  public List<UserStatus> findAllOnlineUsers() {
    return data.values().stream()
        .filter(UserStatus::isCurrentOnline)
        .collect(Collectors.toList());
  }

  @Override
  public void updateLastAccessedAt(UUID userId, Instant lastAccessedAt) {
    UserStatus status = data.get(userId);
    if (status != null) {
      status.updateLastAccessedAt(lastAccessedAt);
    }
  }

  @Override
  public void deleteByUserId(UUID userId) {
    data.remove(userId);
  }
}
