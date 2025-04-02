package com.sprint.mission.discodeit.adapter.outbound.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.port.UserStatusRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {

  Map<UUID, UserStatus> userStatusList = new ConcurrentHashMap<>();

  @Override
  public void save(UserStatus userStatus) {
    userStatusList.put(userStatus.getUserStatusId(), userStatus);
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return userStatusList.values().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public Optional<UserStatus> findByStatusId(UUID userStatusId) {
    return Optional.ofNullable(userStatusList.get(userStatusId));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusList.values().stream().toList();
  }

  @Override
  public void deleteById(UUID userStatusId) {
    userStatusList.remove(userStatusId);
  }

//  @Override
//  public void deleteByUserId(UUID userId) {
//    UserStatus userStatus = findByUserId(userId);
//    userStatusList.remove(userStatus.getUserStatusId());
//  }
}
