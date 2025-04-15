package com.sprint.mission.discodeit.core.status.usecase.user;

import static com.sprint.mission.discodeit.exception.status.user.UserStatusErrors.userStatusAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.status.user.UserStatusErrors.userStatusIdNotFoundError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userIdNotFoundError;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.port.UserStatusRepositoryPort;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UpdateUserStatusCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.status.user.UserStatusError;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserRepositoryPort userRepository;
  private final UserStatusRepositoryPort userStatusRepository;

  @CustomLogging
  @Transactional
  @Override
  public UserStatus create(CreateUserStatusCommand command) {
    User user = userRepository.findById(command.userId()).orElseThrow(
        () -> userIdNotFoundError(command.userId())
    );

    //있으면 진행 X
    if (userStatusRepository.findByUserId(command.userId()).isPresent()) {
      userStatusAlreadyExistsError(command.userId());
    }

    //없으면 진행
    UserStatus userStatus = UserStatus.create(user, command.lastActiveAt());
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
  public boolean isOnline(UUID userId) {
    UserStatus status = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> userStatusIdNotFoundError(userId)
    );
    return status.isOnline();
  }

  @Override
  @CustomLogging
  public void delete(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> userStatusIdNotFoundError(userId)
    );

    userStatusRepository.delete(userStatus.getId());
  }

}
