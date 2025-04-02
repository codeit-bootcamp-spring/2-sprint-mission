package com.sprint.mission.discodeit.core.user.usecase;

import static com.sprint.mission.discodeit.exception.user.UserErrors.userEmailAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userIdNotFoundError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userLoginFailedError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userNameAlreadyExistsError;
import static com.sprint.mission.discodeit.exception.user.UserErrors.userNameNotFoundError;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserListResult;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserStatusService userStatusService;
  private final UserRepositoryPort userRepositoryPort;
  private final BinaryContentRepositoryPort binaryContentRepositoryPort;

  @CustomLogging
  @Override
  public void create(CreateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO) {
    UUID profileId = null;
    validateUser(command.name(), command.email());

    String hashedPassword = BCrypt.hashpw(command.password(), BCrypt.gensalt());

    if (binaryContentDTO.isPresent()) {
      profileId = makeBinaryContent(binaryContentDTO);
    }

    User user = User.create(command.name(), command.email(), hashedPassword, profileId);
    userRepositoryPort.save(user);
    userStatusService.create(new CreateUserStatusCommand(user.getId(), Instant.now()));
  }

  private void validateUser(String name, String email) {
    if (userRepositoryPort.findByName(name).isPresent()) {
      userNameAlreadyExistsError(name);
    }

    if (userRepositoryPort.findByEmail(email).isPresent()) {
      userEmailAlreadyExistsError(email);
    }
  }

  private UUID makeBinaryContent(Optional<CreateBinaryContentCommand> binaryContentDTO) {
    return binaryContentDTO.map(contentDTO -> {
      String fileName = contentDTO.fileName();
      String contentType = contentDTO.contentType();
      byte[] bytes = contentDTO.bytes();
      long size = bytes.length;

      BinaryContent content = BinaryContent.create(fileName, size, contentType, bytes);
      binaryContentRepositoryPort.save(content);

      return content.getId();
    }).orElse(null);
  }

  @CustomLogging
  @Override
  public LoginUserResult login(LoginUserCommand command) {
    List<User> list = userRepositoryPort.findAll();
    User user = list.stream().filter(u -> u.getName().equals(command.userName())).findFirst()
        .orElseThrow(() -> userNameNotFoundError(command.userName()));

    if (!BCrypt.checkpw(command.password(), user.getPassword())) {
      userLoginFailedError(user.getId(), "Password mismatch");
    }
    //TODO. 미구현으로 임시로 "-1"값 넣어둠
    return new LoginUserResult("-1");
  }

  @Override
  public UserResult findById(UUID userId) {
    User user = userRepositoryPort.findById(userId)
        .orElseThrow(() -> userIdNotFoundError(userId));
    boolean online = userStatusService.findByUserId(userId).isOnline();
    return UserResult.create(user, online);
  }

  @Override
  public UserListResult findAll() {
    List<User> userList = userRepositoryPort.findAll();

    return new UserListResult(userList.stream().map(user -> UserResult.create(
        user,
        userStatusService.findByUserId(user.getId()).isOnline())
    ).toList());
  }

  @CustomLogging
  @Override
  public void update(UpdateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO) {

    User user = userRepositoryPort.findById(command.requestUserId())
        .orElseThrow(() -> userIdNotFoundError(command.requestUserId()));
    UUID profileId = user.getProfileId();
    if (profileId != null) {
      binaryContentRepositoryPort.delete(profileId);
    }
    UUID newProfileId = makeBinaryContent(binaryContentDTO);
    user.update(command.newName(), command.newEmail(), newProfileId);

  }

  @CustomLogging
  @Override
  public void delete(UUID userId) {
    User user = userRepositoryPort.findById(userId)
        .orElseThrow(() -> userIdNotFoundError(userId));

    Optional.ofNullable(user.getProfileId())
        .ifPresent(binaryContentRepositoryPort::delete);

    userStatusService.delete(user.getId());
    userRepositoryPort.delete(user.getId());
  }

  @Override
  public boolean existsById(UUID userId) {
    return userRepositoryPort.findById(userId).isPresent();
  }

}
