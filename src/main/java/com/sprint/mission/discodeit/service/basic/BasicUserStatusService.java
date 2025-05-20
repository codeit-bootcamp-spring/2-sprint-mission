package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserStatusService implements UserStatusService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(UserNotFoundException::new);

    if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
      throw new IllegalArgumentException("해당 유저의 userStatus 이미 존재");
    }

    UserStatus userStatus = new UserStatus(user);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(UserStatusNotFoundException::new);
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(UserNotFoundException::new);
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus update(UUID userStatusId, Instant newLastActiveAt) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException());

    userStatus.updateLastActiveAt(newLastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus updateByUserId(UUID userId, Instant newLastActiveAt) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException());

    userStatus.updateLastActiveAt(Instant.now());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new UserStatusNotFoundException();
    }
    userStatusRepository.deleteById(userStatusId);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException());
    userStatusRepository.deleteById(userStatus.getId());
  }
}
