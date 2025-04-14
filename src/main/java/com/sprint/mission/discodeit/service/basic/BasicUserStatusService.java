package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatusDto findById(UUID id) {
    UserStatus status = userStatusRepository.findUserStatusById(id)
        .orElseThrow(() -> new IllegalArgumentException("UserStatus를 찾을 수 없습니다."));
    return convert(status);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAllUserStatus()
        .stream()
        .map(this::convert)
        .collect(Collectors.toList());
  }

  @Override
  public UserStatus create(UUID userId) {
    if (userRepository.findUserById(userId).isEmpty()) {
      throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
    }

    if (userStatusRepository.findUserStatusById(userId).isPresent()) {
      throw new IllegalStateException("이미 상태가 존재합니다.");
    }

    return UserStatus.builder()
        .userid(userId)
        .lastActiveAt(Instant.now())
        .build();
  }

  @Override
  public UserStatus update(UUID userId, UserStatusUpdateRequest request) {
    UserStatus status = userStatusRepository.findUserStatusById(userId)
        .orElseThrow(() -> new IllegalArgumentException("UserStatus를 찾을 수 없습니다."));
    status.setLastActiveAt(request.newLastActiveAt());
    userStatusRepository.addUserStatus(status);

    return status;
  }

  @Override
  public void delete(UUID id) {
    userStatusRepository.deleteUserStatusById(id);
  }

  private UserStatusDto convert(UserStatus status) {
    return new UserStatusDto(status.getUserid(), status.getUserid(), status.getLastActiveAt());
  }
}
