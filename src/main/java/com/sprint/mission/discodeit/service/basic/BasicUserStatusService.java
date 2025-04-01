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
  public UserStatus create(UserStatusCreateRequest requestDto) {
    User user = userRepository.findById(requestDto.userId())
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 없음"));

    if (userStatusRepository.findByUserId(requestDto.userId()).isPresent()) {
      throw new IllegalArgumentException("해당 유저의 userStatus 이미 존재");
    }

    UserStatus userStatus = new UserStatus(requestDto.userId());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID id) {
    return userStatusRepository.findById(id)
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
  public UserStatus update(UUID userId, Instant newLastActiveAt) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 상태 없음"));

    userStatus.updateLastActiveAt(newLastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus updateByUserId(UUID userId) {
    return update(userId, Instant.now());
  }

  @Override
  public void delete(UUID id) {
    if (!userStatusRepository.existsById(id)) {
      throw new ResourceNotFoundException("해당 유저 상태 없음");
    }
    userStatusRepository.deleteById(id);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 상태 없음"));
    userStatusRepository.deleteById(userStatus.getId());
  }
}
