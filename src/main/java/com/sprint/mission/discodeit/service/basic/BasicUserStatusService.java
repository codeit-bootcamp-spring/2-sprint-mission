package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatus create(User user) {
    if (userStatusRepository.findById(user.getId()).isPresent()) {
      throw new DiscodeitException(ErrorCode.USER_STATUS_ALREADY_EXISTS);
    }

    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(Instant.now())
        .build();

    userStatusRepository.save(userStatus);

    return userStatus;
  }

  @Override
  public UserStatusDto findById(UUID UserStatusId) {
    return userStatusMapper.toDto(findUserStatusOrThrow(UserStatusId));
  }

  @Override
  public UserStatusDto findByUserId(UUID UserId) {
    UserStatus status = userStatusRepository.findByUserId(UserId)
        .orElseThrow(
            () -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND));
    return userStatusMapper.toDto(status);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll()
        .stream()
        .map(userStatusMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public UserStatusDto update(UUID userId, UserStatusUpdateRequest request) {
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND));
    status.setLastActiveAt(request.newLastActiveAt());

    return userStatusMapper.toDto(status);
  }

  @Override
  public void delete(UUID UserStatusId) {
    userStatusRepository.deleteById(UserStatusId);
  }

  private UserStatus findUserStatusOrThrow(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId).orElseThrow(
        () -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND));
  }
}
