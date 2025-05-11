package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.DuplicateUserStatusException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.HashMap;
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

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("user 찾기 실패 - ID: {}", userId);
          Map<String, Object> details = new HashMap<>();
          details.put("userId", userId);
          return new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, details);
        });
    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          log.warn("이미 존재하는 userStatus 입니다. userId: {}", userId);
          Map<String, Object> details = new HashMap<>();
          details.put("userId", userId);
          throw new DuplicateUserStatusException(Instant.now(), ErrorCode.DUPLICATE_USERSTATUS, details);
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(
            () -> {
              log.warn("UserStatus 찾기 실패. userStatusId: {}", userStatusId);
              Map<String, Object> details = new HashMap<>();
              details.put("userStatusId", userStatusId);
              return new UserStatusNotFoundException(Instant.now(), ErrorCode.USERSTATUS_NOTFOUND, details);
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

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> {
              log.warn("UserStatus 수정 실패 - 존재하지 않는 userStatusId: {}", userStatusId);
              Map<String, Object> details = new HashMap<>();
              details.put("userStatusId", userStatusId);
              return new UserStatusNotFoundException(Instant.now(), ErrorCode.USERSTATUS_NOTFOUND, details);
            });
    userStatus.update(newLastActiveAt);

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> {
              log.warn("UserStatus 수정 실패 - 존재하지 않는 userStatus - userId: {}", userId);
              Map<String, Object> details = new HashMap<>();
              details.put("userId", userId);
              return new UserStatusNotFoundException(Instant.now(), ErrorCode.USERSTATUS_NOTFOUND, details);
            });
    userStatus.update(newLastActiveAt);

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      log.warn("UserStatus 삭제 실패 - 존재하지 않는 userStatusId: {}", userStatusId);
      Map<String, Object> details = new HashMap<>();
      details.put("userStatusId", userStatusId);
      throw new UserStatusNotFoundException(Instant.now(), ErrorCode.USERSTATUS_NOTFOUND, details);
    }
    userStatusRepository.deleteById(userStatusId);
  }
}
