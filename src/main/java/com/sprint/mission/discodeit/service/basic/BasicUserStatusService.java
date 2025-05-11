package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.userStatus.CreateUserStatusCommand;
import com.sprint.mission.discodeit.dto.service.userStatus.CreateUserStatusResult;
import com.sprint.mission.discodeit.dto.service.userStatus.FindUserStatusResult;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.DuplicateUserStatusException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserStatusService implements UserStatusService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional
  public CreateUserStatusResult create(CreateUserStatusCommand createUserStatusCommand) {
    User user = checkUserExists(createUserStatusCommand);
    checkDuplicateUser(createUserStatusCommand);

    UserStatus userStatus = new UserStatus(user, Instant.now());
    userStatusRepository.save(userStatus);

    user.updateUserStatus(userStatus);
    return userStatusMapper.toCreateUserStatusResult(userStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public FindUserStatusResult findById(UUID id) {
    UserStatus userStatus = findUserStatusById(id);
    return userStatusMapper.toFindUserStatusResult(userStatus);
  }

  @Override
  @Transactional
  @CachePut(value = "user", key = "#p0")
  @CacheEvict(value = "allUsers", allEntries = true)
  public UpdateUserStatusResult updateByUserId(UUID userId) {
    UserStatus userStatus = findUserStatusByUserId(userId, "update");
    userStatus.updateUserStatus();

    User user = userStatus.getUser();
    user.updateUserStatus(userStatus);
    return userStatusMapper.toUpdateUserStatusResult(userStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public FindUserStatusResult findByUserId(UUID userId) {
    UserStatus userStatus = findUserStatusByUserId(userId, "find");
    return userStatusMapper.toFindUserStatusResult(userStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FindUserStatusResult> findAll() {
    List<UserStatus> userStatuses = userStatusRepository.findAll();

    return userStatuses.stream()
        .map(userStatusMapper::toFindUserStatusResult)
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void deleteByUserId(UUID userId) {
    userStatusRepository.deleteByUserId(userId);
  }


  private User checkUserExists(CreateUserStatusCommand createUserStatusCommand) {
    return userRepository.findById(createUserStatusCommand.userId())
        .orElseThrow(() -> {
          log.warn("UserStatus create failed: user not found (userId: {})",
              createUserStatusCommand.userId());
          return new UserNotFoundException(Map.of("userId", createUserStatusCommand.userId()));
        });
  }

  private void checkDuplicateUser(CreateUserStatusCommand createUserStatusCommand) {
    if (userStatusRepository.existsByUserId(createUserStatusCommand.userId())) {
      log.warn("UserStatus create failed: duplicate userStatus (userId: {})",
          createUserStatusCommand.userId());
      throw new DuplicateUserStatusException(Map.of("userId", createUserStatusCommand.userId()));
    }
  }

  private UserStatus findUserStatusByUserId(UUID userId, String method) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> {
          log.warn("UserStatus {} failed: userStatus not found (userId: {}", method, userId);
          return new UserStatusNotFoundException(Map.of("userId", userId, "method", method));
        });
  }

  private UserStatus findUserStatusById(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("UserStatus find failed: userStatus not found (userStatusId: {}", id);
          return new UserStatusNotFoundException(Map.of("userStatusId", id));
        });
  }
}
