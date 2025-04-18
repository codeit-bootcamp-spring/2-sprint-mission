package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("UserId: " + userId + " not found"));

    if (userStatusRepository.findById(userId).isPresent()) {
      throw new IllegalStateException("이미 상태가 존재합니다.");
    }

    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(Instant.now())
        .build();

    userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto findById(UUID UserStatusId) {
    return userStatusMapper.toDto(findUserStatusOrThrow(UserStatusId));
  }

  @Override
  public UserStatusDto findByUserId(UUID UserId) {
    UserStatus status = userStatusRepository.findByUserId(UserId)
        .orElseThrow(
            () -> new NoSuchElementException("UserId: " + UserId + " UserStatus not found"));
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
    UserStatus status = findUserStatusOrThrow(userId);
    status.setLastActiveAt(request.newLastActiveAt());

    return userStatusMapper.toDto(status);
  }

  @Override
  public void delete(UUID UserStatusId) {
    userStatusRepository.deleteById(UserStatusId);
  }

  private UserStatus findUserStatusOrThrow(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId).orElseThrow(
        () -> new NoSuchElementException("UserStatusId: " + userStatusId + " not found"));
  }
}
