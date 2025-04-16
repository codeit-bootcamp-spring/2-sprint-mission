package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mepper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
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

  @Override
  public UserStatusDto findById(UUID UserStatusId) {
    UserStatus status = userStatusRepository.findById(UserStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("UserStatusId: " + UserStatusId + " not found"));
    return userStatusMapper.toDto(status);
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
  public UserStatus create(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("UserId: " + userId + " not found"));

    if (userStatusRepository.findById(userId).isPresent()) {
      throw new IllegalStateException("이미 상태가 존재합니다.");
    }

    return UserStatus.builder()
        .user(user)
        .lastActiveAt(Instant.now())
        .build();
  }

  @Override
  public UserStatus update(UUID userId, UserStatusUpdateRequest request) {
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new NoSuchElementException("UserId: " + userId + " UserStatus not found"));
    status.setLastActiveAt(request.newLastActiveAt());

    return status;
  }

  @Override
  public void delete(UUID UserStatusId) {
    userStatusRepository.deleteById(UserStatusId);
  }
}
