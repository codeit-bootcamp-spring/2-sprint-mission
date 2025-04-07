package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 없음"));

    if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
      throw new IllegalArgumentException("해당 유저의 userStatus 이미 존재");
    }

    UserStatus userStatus = new UserStatus(request.userId());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 상태 없음"));
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 없음"));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus update(UUID userStatusId, Instant newLastActiveAt) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 상태 없음"));

    userStatus.updateLastActiveAt(newLastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus updateByUserId(UUID userId, Instant newLastActiveAt) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 상태 없음"));

    userStatus.updateLastActiveAt(Instant.now());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new ResourceNotFoundException("해당 유저 상태 없음");
    }
    userStatusRepository.deleteById(userStatusId);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 상태 없음"));
    userStatusRepository.deleteById(userStatus.getId());
  }
}
