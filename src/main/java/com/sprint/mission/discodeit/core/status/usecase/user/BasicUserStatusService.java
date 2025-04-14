package com.sprint.mission.discodeit.core.status.usecase.user;

import static com.sprint.mission.discodeit.exception.status.user.UserStatusErrors.userStatusAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.status.user.UserStatusErrors.userStatusIdNotFoundError;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.port.UserStatusRepositoryPort;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UpdateUserStatusCommand;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.status.user.UserStatusError;
import com.sprint.mission.discodeit.exception.user.UserErrors;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserRepositoryPort userRepositoryPort;
  private final UserStatusRepositoryPort userStatusRepositoryPort;

  @Override
  @CustomLogging
  public UserStatus create(CreateUserStatusCommand command) {
    if (userRepositoryPort.findById(command.userId()).isEmpty()) {
      UserErrors.userIdNotFoundError(command.userId());
    }

    //있으면 진행 X
    if (userStatusRepositoryPort.findByUserId(command.userId()).isPresent()) {
      userStatusAlreadyExistsError(command.userId());
    }

    //없으면 진행
    UserStatus userStatus = UserStatus.create(command.userId(), command.lastActiveAt());
    userStatusRepositoryPort.save(userStatus);
    return userStatus;
  }


  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepositoryPort.findByUserId(userId)
        .orElseThrow(() -> userStatusIdNotFoundError(userId));

  }

  @Override
  public UserStatus findByStatusId(UUID userStatusId) {
    return userStatusRepositoryPort.findByStatusId(userStatusId)
        .orElseThrow(() -> userStatusIdNotFoundError(userStatusId));

  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepositoryPort.findAll();
  }

  @Override
  @CustomLogging
  public UserStatus update(UpdateUserStatusCommand command) {
    UserStatus userStatus;
    if (command.userId() != null) {
      userStatus = userStatusRepositoryPort.findByUserId(command.userId()).orElseThrow(
          () -> userStatusIdNotFoundError(command.userId())
      );

    } else if (command.userStatusId() != null) {
      userStatus = userStatusRepositoryPort.findByStatusId(command.userStatusId()).orElseThrow(
          () -> userStatusIdNotFoundError(command.userStatusId())
      );
    } else {
      throw new UserStatusError("Update Error");
    }

    userStatus.update(command.newLastActiveAt());
    return userStatus;
  }

  @Override
  @CustomLogging
  public void delete(UUID userId) {
    UserStatus userStatus = userStatusRepositoryPort.findByUserId(userId).orElseThrow(
        () -> userStatusIdNotFoundError(userId)
    );

    userStatusRepositoryPort.delete(userStatus.getId());
  }

}
