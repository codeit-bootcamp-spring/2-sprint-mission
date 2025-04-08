package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusRepository;

import java.util.*;


public class UserStatusRepositoryImplement implements UserStatusRepository {

  private final Map<UUID, UserStatus> userStatuses = new HashMap<>();

  @Override
  public void saveData() {

  }

  @Override
  public void register(UserStatus userStatus) {
    userStatuses.put(userStatus.getId(), userStatus);
  }

  // status 아이디로 찾기
  @Override
  public Optional<UserStatus> findById(UUID id) {
    return Optional.ofNullable(userStatuses.get(id));
  }

  // 유저 아이디로 찾기
  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return userStatuses.values().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return new ArrayList<>(userStatuses.values());
  }

  @Override
  public boolean update(UserStatus userStatus) {
    if (userStatuses.containsKey(userStatus.getId())) {
      userStatuses.put(userStatus.getId(), userStatus);
      return true;
    }
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    return userStatuses.remove(id) != null;
  }
} 