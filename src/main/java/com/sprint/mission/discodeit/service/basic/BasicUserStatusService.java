package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatus create(UserStatusCreateRequestDto dto) {
    User user = Optional.ofNullable(userRepository.findById(dto.getUserId()))
        .orElseThrow(
            () -> new NoSuchElementException("User with id " + dto.getUserId() + " not found"));

    if (userStatusRepository.findAll().stream()
        .anyMatch(userStatus -> userStatus.getUserId().equals(user.getId()))) {
      throw new IllegalArgumentException("관련된 객체가 이미 존재합니다.");
    }

    UserStatus userStatus = new UserStatus(dto.getUserId(), dto.getActivatedAt());
    userStatusRepository.save(userStatus);

    return userStatus;
  }

  @Override
  public UserStatus findById(UUID userStatusId) {
    return Optional.ofNullable(userStatusRepository.findById(userStatusId))
        .orElseThrow(() -> new UserStatusNotFoundException(
            "UserStatus with id " + userStatusId + " not found"));
  }

  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  public UserStatus update(UUID userId, UserStatusUpdateRequest request) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId);
    if (userStatus == null) {
      throw new UserStatusNotFoundException("UserStatus with userId " + userId + " not found");
    }

    return userStatusRepository.update(userStatus.getId(), request);
  }

  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    System.out.println(">>> 요청 들어옴 - userId: " + userId);
    System.out.println(">>> 변경할 활성화 시간: " + request.newLastActiveAt());

    if (request.newLastActiveAt() == null) {
      throw new IllegalArgumentException("newActivatedAt 값이 null입니다.");
    }

    UserStatus userStatus = userStatusRepository.findAll().stream()
        .filter(status -> status.getUserId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new UserStatusNotFoundException(
            "UserStatus with userid " + userId + " not found"));

    return userStatusRepository.update(userStatus.getId(), request);
  }

  public void delete(UUID userStatusId) {
    userStatusRepository.delete(userStatusId);
  }
}
