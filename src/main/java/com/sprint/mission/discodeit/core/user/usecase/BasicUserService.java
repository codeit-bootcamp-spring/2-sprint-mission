package com.sprint.mission.discodeit.core.user.usecase;

import static com.sprint.mission.discodeit.exception.user.UserErrors.userEmailAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userIdNotFoundError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userLoginFailedError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userNameAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userNameNotFoundError;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.OnlineUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UpdateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserListResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(BasicUserService.class);

  private final UserStatusService userStatusService;
  private final UserRepositoryPort userRepositoryPort;
  private final BinaryContentRepositoryPort binaryContentRepositoryPort;

  @Transactional
  @Override
  public UserResult create(CreateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO) {
    BinaryContent profile = null;
    validateUser(command.username(), command.email());

//    String hashedPassword = BCrypt.hashpw(command.password(), BCrypt.gensalt());

    if (binaryContentDTO.isPresent()) {
      profile = makeBinaryContent(binaryContentDTO);
    }

    User user = User.create(command.username(), command.email(), command.password(), profile);
    userRepositoryPort.save(user);
    logger.info("User registered: {}", user.getId());

    UserStatusResult statusResult = userStatusService.create(
        new CreateUserStatusCommand(user.getId(), Instant.now()));
    logger.info("User Status created: {}", statusResult.id());

    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  private void validateUser(String name, String email) {
    if (userRepositoryPort.findByName(name).isPresent()) {
      userNameAlreadyExistsError(name);
    }

    if (userRepositoryPort.findByEmail(email).isPresent()) {
      userEmailAlreadyExistsError(email);
    }
  }

  private BinaryContent makeBinaryContent(Optional<CreateBinaryContentCommand> binaryContentDTO) {
    return binaryContentDTO.map(contentDTO -> {
      String fileName = contentDTO.fileName();
      String contentType = contentDTO.contentType();
      byte[] bytes = contentDTO.bytes();
      Long size = (long) bytes.length;

      BinaryContent content = BinaryContent.create(fileName, size, contentType, bytes);
      binaryContentRepositoryPort.save(content);
      logger.info("Binary Content Created: {}", content.getId());

      return content;
    }).orElse(null);
  }

  @Override
  public UserResult login(LoginUserCommand command) {
    User user = userRepositoryPort.findByName(command.username()).orElseThrow(
        () -> userNameNotFoundError(command.username())
    );

    if (!command.password().equals(user.getPassword())) {
      userLoginFailedError(user.getId(), "Password mismatch");
    }

    logger.info("User login: id {}, username {}, password  {}", user.getId(), user.getName(),
        user.getPassword());

    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  @Override
  public UserResult findById(UUID userId) {
    User user = userRepositoryPort.findById(userId)
        .orElseThrow(() -> userIdNotFoundError(userId));
    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  @Override
  public UserListResult findAll() {
    List<User> userList = userRepositoryPort.findAll();

    return new UserListResult(userList.stream().map(user -> UserResult.create(
        user,
        userStatusService.isOnline(user.getId()))
    ).toList());
  }

  @CustomLogging
  @Override
  public UserResult update(UpdateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO) {

    User user = userRepositoryPort.findById(command.requestUserId())
        .orElseThrow(() -> userIdNotFoundError(command.requestUserId()));

    BinaryContent profile = user.getProfile();
    if (profile != null) {
      binaryContentRepositoryPort.delete(profile.getId());
    }
    BinaryContent newProfile = makeBinaryContent(binaryContentDTO);
    user.update(command.newName(), command.newEmail(), command.newPassword(), newProfile);
    logger.info("User Updated: username {}, email {}, password {}", user.getName(), user.getEmail(),
        user.getPassword());
    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  @CustomLogging
  @Override
  public void delete(UUID userId) {
    User user = userRepositoryPort.findById(userId)
        .orElseThrow(() -> userIdNotFoundError(userId));

    Optional.ofNullable(user.getProfile().getId())
        .ifPresent(binaryContentRepositoryPort::delete);

    userStatusService.delete(user.getId());
    userRepositoryPort.delete(user.getId());

    logger.info("User deleted {}", userId);
  }

  @Override
  public boolean existsById(UUID userId) {
    return userRepositoryPort.findById(userId).isPresent();
  }

  @Override
  public UserStatusResult online(OnlineUserStatusCommand command) {
    User user = userRepositoryPort.findById(command.userId()).orElseThrow(
        () -> userIdNotFoundError(command.userId())
    );
    UserStatusResult statusResult = userStatusService.findByUserId(user.getId());

    return userStatusService.update(
        new UpdateUserStatusCommand(statusResult.userId(), statusResult.userId(),
            command.lastActiveAt()));
  }
}
