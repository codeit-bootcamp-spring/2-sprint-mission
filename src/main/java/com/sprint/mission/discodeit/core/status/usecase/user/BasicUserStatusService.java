package com.sprint.mission.discodeit.core.status.usecase.user;

import static com.sprint.mission.discodeit.exception.status.user.UserStatusErrors.userStatusAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.status.user.UserStatusErrors.userStatusIdNotFoundError;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.port.UserStatusRepository;
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
  private final UserStatusRepository userStatusRepository;

  @Override
  @CustomLogging
  public UserStatus create(CreateUserStatusCommand command) {
    if (userRepositoryPort.findById(command.userId()).isEmpty()) {
      UserErrors.userIdNotFoundError(command.userId());
    }

    //있으면 진행 X
    if (userStatusRepository.findByUserId(command.userId()).isPresent()) {
      userStatusAlreadyExistsError(command.userId());
    }
    //없으면 진행
    UserStatus userStatus = UserStatus.create(command.userId(), command.lastActiveAt());
    userStatusRepository.save(userStatus);
    return userStatus;
  }


  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> userStatusIdNotFoundError(userId));

  }

  @Override
  public UserStatus findByStatusId(UUID userStatusId) {
    return userStatusRepository.findByStatusId(userStatusId)
        .orElseThrow(() -> userStatusIdNotFoundError(userStatusId));

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
          () -> userStatusIdNotFoundError(command.userId())
      );

    } else if (command.userStatusId() != null) {
      userStatus = userStatusRepository.findByStatusId(command.userStatusId()).orElseThrow(
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
  public void delete(UUID userStatusId) {
    userStatusRepository.deleteById(userStatusId);
  }

}
