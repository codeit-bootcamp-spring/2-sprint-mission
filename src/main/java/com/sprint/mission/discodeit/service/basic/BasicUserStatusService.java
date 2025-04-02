package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusInfoDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  public BasicUserStatusService(UserStatusRepository userStatusRepository,
      UserRepository userRepository) {
    this.userStatusRepository = userStatusRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UserStatusInfoDto findById(UUID id) {
    UserStatus status = userStatusRepository.findUserStatusById(id)
        .orElseThrow(() -> new IllegalArgumentException("UserStatus를 찾을 수 없습니다."));
    return convert(status);
  }

  @Override
  public List<UserStatusInfoDto> findAll() {
    return userStatusRepository.findAllUserStatus()
        .stream()
        .map(this::convert)
        .collect(Collectors.toList());
  }

  @Override
  public void create(UUID userId) {
    if (userRepository.findUserById(userId) == null) {
      throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
    }

    if (userStatusRepository.findUserStatusById(userId).isPresent()) {
      throw new IllegalStateException("이미 상태가 존재합니다.");
    }

    UserStatus status = new UserStatus(userId, Instant.now());
    userStatusRepository.addUserStatus(status);
  }

  @Override
  public void update(UUID userId) {
    UserStatus status = userStatusRepository.findUserStatusById(userId)
        .orElseThrow(() -> new IllegalArgumentException("UserStatus를 찾을 수 없습니다."));
    status.updateLastActiveAt();
    userStatusRepository.addUserStatus(status);
  }

  @Override
  public void delete(UUID id) {
    userStatusRepository.deleteUserStatusById(id);
  }

  private UserStatusInfoDto convert(UserStatus status) {
    return new UserStatusInfoDto(status.getUserid(), status.getUserid(), status.isUserOnline(),
        status.getUpdatedAt());
  }
}
