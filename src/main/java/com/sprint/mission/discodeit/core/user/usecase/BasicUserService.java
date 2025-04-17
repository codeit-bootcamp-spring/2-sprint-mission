package com.sprint.mission.discodeit.core.user.usecase;

import static com.sprint.mission.discodeit.exception.user.UserErrors.userEmailAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userIdNotFoundError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userLoginFailedError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userNameAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userNameNotFoundError;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentMetaRepositoryPort;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
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

  private final UserRepositoryPort userRepository;
  private final UserStatusService userStatusService;

  private final BinaryContentStoragePort binaryContentStorage;
  private final BinaryContentMetaRepositoryPort binaryContentMetaRepository;

  @Override
  @Transactional
  public UserResult create(CreateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO) {
    BinaryContent profile = null;
    validateUser(command.username(), command.email());

    if (binaryContentDTO.isPresent()) {
      profile = makeBinaryContent(binaryContentDTO);
    }

    User user = User.create(command.username(), command.email(), command.password(), profile);
    userRepository.save(user);
    logger.info("User registered: {}", user.getId());

    UserStatus userStatus = userStatusService.create(
        new CreateUserStatusCommand(user.getId(), Instant.now()));
    logger.info("User Status created: {}", userStatus.getId());
    user.setUserStatus(userStatus);

    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  private void validateUser(String name, String email) {
    if (userRepository.findByName(name).isPresent()) {
      userNameAlreadyExistsError(name);
    }

    if (userRepository.findByEmail(email).isPresent()) {
      userEmailAlreadyExistsError(email);
    }
  }

  private BinaryContent makeBinaryContent(Optional<CreateBinaryContentCommand> binaryContentDTO) {
    return binaryContentDTO.map(contentDTO -> {
      String fileName = contentDTO.fileName();
      String contentType = contentDTO.contentType();
      byte[] bytes = contentDTO.bytes();
      Long size = (long) bytes.length;

      BinaryContent content = BinaryContent.create(fileName, size, contentType);

      binaryContentMetaRepository.save(content);
      binaryContentStorage.put(content.getId(), bytes);

      logger.info("Binary Content Created: {}", content.getId());

      return content;
    }).orElse(null);
  }

  @Override
  public UserResult login(LoginUserCommand command) {
    User user = userRepository.findByName(command.username()).orElseThrow(
        () -> userNameNotFoundError(command.username())
    );

    if (!command.password().equals(user.getPassword())) {
      userLoginFailedError(user.getId(), "Password mismatch");
    }

    user.getUserStatus().updateTime(Instant.now());

    logger.info("User login: id {}, username {}, password  {}", user.getId(), user.getName(),
        user.getPassword());

    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  @Override
  @Transactional(readOnly = true)
  public UserResult findById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> userIdNotFoundError(userId));
    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  @Override
  @Transactional(readOnly = true)
  public UserListResult findAll() {
    List<User> userList = userRepository.findAll();

    return new UserListResult(userList.stream().map(user -> UserResult.create(
        user,
        user.getUserStatus().isOnline())
    ).toList());
  }

  @CustomLogging
  @Override
  @Transactional
  public UserResult update(UpdateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO) {

    User user = userRepository.findById(command.requestUserId())
        .orElseThrow(() -> userIdNotFoundError(command.requestUserId()));

    BinaryContent profile = user.getProfile();
    if (profile != null) {
      binaryContentMetaRepository.delete(profile.getId());
    }
    BinaryContent newProfile = makeBinaryContent(binaryContentDTO);

    user.update(command.newName(), command.newEmail(), command.newPassword(), newProfile);
    logger.info("User Updated: username {}, email {}, password {}", user.getName(), user.getEmail(),
        user.getPassword());
    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  @CustomLogging
  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> userIdNotFoundError(userId));

    Optional.ofNullable(user.getProfile().getId())
        .ifPresent(binaryContentMetaRepository::delete);

    userStatusService.delete(user.getId());
    userRepository.delete(user.getId());

    logger.info("User deleted {}", userId);
  }

  @Override
  public boolean existsById(UUID userId) {
    return userRepository.findById(userId).isPresent();
  }

  @Override
  public UserStatusResult online(OnlineUserStatusCommand command) {
    User user = userRepository.findById(command.userId()).orElseThrow(
        () -> userIdNotFoundError(command.userId())
    );
    UserStatus userStatus = userStatusService.findByUserId(user.getId());

    UserStatus update = userStatusService.update(
        new UpdateUserStatusCommand(userStatus.getUser().getId(), userStatus.getId(),
            command.lastActiveAt()));

    return UserStatusResult.create(update);
  }
}
