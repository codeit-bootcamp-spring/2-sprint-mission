package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.DuplicateUserStatusException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto createUserStatus(UserStatusCreateRequest request) {
    UUID userId = request.userId();
    Instant lastActiveAt = request.lastActiveAt();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.byId(userId));

    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          throw DuplicateUserStatusException.byUserId(userId);
        });

    UserStatus userStatus = UserStatus.create(user, lastActiveAt);
    UserStatus createdUserStatus = userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(createdUserStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public UserStatusDto findById(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(
            () -> UserStatusNotFoundException.byId(userStatusId));
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> UserStatusNotFoundException.byId(userStatusId));
    checkUserExists(userStatus.getUser().getId());

    userStatus.update(request.newLastActiveAt());
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest request) {
    checkUserExists(userId);
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> UserStatusNotFoundException.byUserId(userId));

    userStatus.update(request.newLastActiveAt());
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void deleteUserStatus(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> UserStatusNotFoundException.byId(userStatusId));
    checkUserExists(userStatus.getUser().getId());

    userStatusRepository.deleteById(userStatusId);
  }


  /*******************************
   * Validation check
   *******************************/
  private void checkUserExists(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw UserNotFoundException.byId(userId);
    }
  }

}
