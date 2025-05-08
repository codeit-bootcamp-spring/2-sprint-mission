package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.repository.JpaUserStatusRepository;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UpdateUserStatusCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.UserException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserStatusService implements UserStatusService {

  private final JpaUserStatusRepository userStatusRepository;

  @Transactional
  @Override
  public UserStatus create(CreateUserStatusCommand command) {
    User user = command.user();
    if (userStatusRepository.findByUser_Id(user.getId()).isPresent()) {
      log.warn("[UserStatusService] User Status is already Existed : user Id {}", user.getId());
      throw new UserAlreadyExistsException(ErrorCode.USER_STATUS_ALREADY_EXISTS,
          user.getUserStatus().getId());
    }

    UserStatus userStatus = UserStatus.create(user, command.lastActiveAt());
    userStatusRepository.save(userStatus);
    log.info("[UserStatusService] User Status created : id {}, user id {}, last Active At {}",
        userStatus.getId(),
        user.getId(), userStatus.getLastActiveAt());

    return userStatus;
  }

  @Override
  @Transactional
  public UserStatus update(UpdateUserStatusCommand command) {
    UserStatus userStatus;
    if (command.userId() != null) {
      userStatus = userStatusRepository.findByUser_Id(command.userId()).orElseThrow(
          () -> new UserNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, command.userId())
      );
    } else if (command.userStatusId() != null) {
      userStatus = userStatusRepository.findById(command.userStatusId()).orElseThrow(
          () -> new UserNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, command.userStatusId())
      );
    } else {
      log.warn("[UserStatusService] Bad Request");
      throw new UserException(ErrorCode.USER_NOT_FOUND);
    }
    userStatus.update(command.newLastActiveAt());
    log.info("[UserStatusService] User Status updated : id {}, last Active At {}",
        userStatus.getId(),
        userStatus.getLastActiveAt());
    return userStatus;
  }

  @Override
  public boolean isOnline(UUID userId) {
    UserStatus status = userStatusRepository.findByUser_Id(userId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, userId)
    );
    return status.isOnline();
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUser_Id(userId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, userId)
    );
    userStatusRepository.deleteById(userStatus.getId());
    log.info("[UserStatusService] User Status deleted : id {}", userStatus.getId());
  }

}
