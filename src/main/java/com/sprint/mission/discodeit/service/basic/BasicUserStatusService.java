package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    if (userRepository.findByKey(request.userId()) == null) {
      throw new IllegalArgumentException("[Error] user is null");
    }
    if (userStatusRepository.findByUserKey(request.userId()) != null) {
      throw new IllegalArgumentException("[Error] userStatus is already exists");
    }
    UserStatus userStatus = new UserStatus(request.userId(), Instant.now());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID userStatusKey) {
    UserStatus userStatus = userStatusRepository.findByKey(userStatusKey);
    if (userStatus == null) {
      throw new IllegalArgumentException("[Error] userStatus is null");
    }
    return userStatus;
  }

  @Override
  public List<UserStatus> findAll() {
    List<UserStatus> userStatuses = userStatusRepository.findAll();
    if (userStatuses == null) {
      throw new IllegalArgumentException("[Error] userStatus is null");
    }
    return userStatuses;
  }

  @Override
  public UserStatus update(UUID userStatusKey, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findByKey(userStatusKey);
    if (userStatus == null) {
      throw new IllegalArgumentException("[Error] userStatus is null");
    }
    userStatus.updateLastActiveAt(request.newLastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus updateByUserKey(UUID userKey, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findByUserKey(userKey);
    if (userStatus == null) {
      throw new IllegalArgumentException("[Error] userStatus is null");
    }
    userStatus.updateLastActiveAt(request.newLastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public void delete(UUID userStatusKey) {
    UserStatus userStatus = userStatusRepository.findByKey(userStatusKey);
    if (userStatus == null) {
      throw new IllegalArgumentException("[Error] userStatus is null");
    }
    userStatusRepository.delete(userStatusKey);
  }

  @Override
  public void deleteByUserKey(UUID userKey) {
    UserStatus userStatus = userStatusRepository.findByUserKey(userKey);
    if (userStatus == null) {
      throw new IllegalArgumentException("[Error] userStatus is null");
    }

    userStatusRepository.delete(userStatus.getId());
  }
}
