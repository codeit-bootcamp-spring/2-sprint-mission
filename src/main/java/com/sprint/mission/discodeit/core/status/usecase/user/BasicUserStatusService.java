package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UpdateUserStatusCommand;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.status.port.UserStatusRepository;
import com.sprint.mission.discodeit.exception.user.DuplicateUserStatusException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import com.sprint.mission.discodeit.exception.user.UserStatusNotFoundException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserRepositoryPort userRepositoryPort;
  private final UserStatusRepository userStatusRepository;

  @Override
  @CustomLogging
  public UserStatus create(CreateUserStatusCommand command) {
    if (userRepositoryPort.findById(command.userId()).isEmpty()) {
      throw new UserNotFoundError("유저가 존재하지 않습니다.");
    }

    //있으면 진행 X
    if (userStatusRepository.findByUserId(command.userId()).isPresent()) {
      throw new DuplicateUserStatusException("유저가 이미 존재합니다.");
    }
    //없으면 진행
    UserStatus userStatus = UserStatus.create(command.userId(), command.lastActiveAt());
    userStatusRepository.save(userStatus);
    return userStatus;
  }


  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾을 수 없습니다."));

  }

  @Override
  public UserStatus findByStatusId(UUID userStatusId) {
    return userStatusRepository.findByStatusId(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾을 수 없습니다."));

  }


  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  @CustomLogging
  public UserStatus update(UpdateUserStatusCommand command) {
    UserStatus userStatus;
    if (command.userId() != null) {
      userStatus = userStatusRepository.findByUserId(command.userId()).orElseThrow(
          () -> new UserStatusNotFoundException("유저 상태를 찾을 수 없습니다.")
      );
    } else if (command.userStatusId() != null) {
      userStatus = userStatusRepository.findByStatusId(command.userStatusId()).orElseThrow(
          () -> new UserStatusNotFoundException("유저 상태를 찾을 수 없습니다.")
      );
    } else {
      throw new RuntimeException("잘못된 값입니다.");
    }

    userStatus.update(command.newLastActiveAt());
    return userStatus;
  }

  @Override
  @CustomLogging
  public void delete(UUID userStatusId) {
    userStatusRepository.deleteById(userStatusId);
  }

}
