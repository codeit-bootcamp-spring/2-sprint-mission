package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.exception.UserStatusException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    log.info("Attempting to create userStatus for user with id: {}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("User with id {} not found", userId);
          return UserException.userNotFound(Map.of("userId", userId));
        });

    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          log.error("User with id {} already has a status", userId);
          throw UserStatusException.userStatusAlreadyExist(Map.of("userId", userId));
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);

    log.info("UserStatus for user with id {} successfully created", userId);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> {
          log.error("UserStatus with id {} not found", userStatusId);
          return UserStatusException.userStatusNotFound(Map.of("userStatusId", userStatusId));
        });
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    log.info("Attempting to update userStatus with id: {}", userStatusId);

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> {
          log.error("UserStatus with id {} not found", userStatusId);
          return UserStatusException.userStatusNotFound(Map.of("userStatusId", userStatusId));
        });

    userStatus.update(newLastActiveAt);

    log.info("UserStatus with id {} successfully updated ", userStatusId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    log.info("Attempting to update userStatus for user with id: {}", userId);

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> {
          log.error("UserStatus for user with id {} not found", userId);
          return UserStatusException.userStatusNotFound(Map.of("userId", userId));
        });

    userStatus.update(newLastActiveAt);

    log.info("UserStatus for user with id {} successfully updated ", userId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    log.info("Attempting to delete userStatus with id: {}", userStatusId);

    if (!userStatusRepository.existsById(userStatusId)) {
      log.error("UserStatus with id {} not found", userStatusId);
      throw UserStatusException.userStatusNotFound(Map.of("userStatusId", userStatusId));
    }

    userStatusRepository.deleteById(userStatusId);
    log.info("Successfully deleted userStatus with id: {}", userStatusId);
  }
}
