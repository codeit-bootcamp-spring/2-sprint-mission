package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.user.usecase.LoginUserResult;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.LoginUserCommand;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserListResult;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserResult;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsError;
import com.sprint.mission.discodeit.exception.user.UserLoginFailedError;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.util.CommonUtils;
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
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO) {
    UUID profileId = null;
    validateUser(command.name(), command.email());

    String hashedPassword = BCrypt.hashpw(command.password(), BCrypt.gensalt());
    if (binaryContentDTO.isPresent()) {
      profileId = makeBinaryContent(binaryContentDTO);
    }

    User user = User.create(command.name(), command.email(), hashedPassword, profileId);
    userRepositoryPort.save(user);
    userStatusService.create(user.getId());
  }

  // TODO. 에러 던질 때 import static 로 하기
  private void validateUser(String name, String email) {
    if (userRepositoryPort.findByName(name).isPresent() || userRepositoryPort.findByEmail(email)
        .isPresent()) {
      throw new UserAlreadyExistsError("동일한 유저가 존재합니다.");
    }
  }

  private UUID makeBinaryContent(Optional<BinaryContentCreateRequestDTO> binaryContentDTO) {
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
    User user = CommonUtils.findByName(list, command.userName(), User::getName)
        .orElseThrow(() -> new UserNotFoundError("로그인 실패: 해당 유저를 찾지 못했습니다."));

    if (!BCrypt.checkpw(command.password(), user.getPassword())) {
      throw new UserLoginFailedError("로그인 실패: 비밀번호가 틀립니다.");
    }
    //미구현으로 임시로 "-1"값 넣어둠
    return new LoginUserResult("-1");
  }

  // TODO. 에러 던질 때 import static 로 하기
  @Override
  public UserResult findById(UUID userId) {
    User user = userRepositoryPort.findById(userId)
        .orElseThrow(() -> new UserNotFoundError("유저가 존재하지 않습니다."));
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
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO) {

    User user = userRepositoryPort.findById(command.requestUserId())
        .orElseThrow(() -> new UserNotFoundError("유저가 존재하지 않습니다."));
    UUID profileId = user.getProfileId();
    if (profileId != null) {
      binaryContentRepositoryPort.delete(profileId);
    }
    UUID newProfileId = makeBinaryContent(binaryContentDTO);
    user.update(command.newName(), command.newEmail(), newProfileId);

  }

  // TODO. 에러 던질 때 import static 로 하기
  @CustomLogging
  @Override
  public void delete(UUID userId) {
    User user = userRepositoryPort.findById(userId)
        .orElseThrow(() -> new UserNotFoundError("유저가 존재하지 않습니다."));

    Optional.ofNullable(user.getProfileId())
        .ifPresent(binaryContentRepositoryPort::delete);

    userStatusService.deleteById(user.getId());
    userRepositoryPort.delete(user.getId());
  }

  @Override
  public boolean existsById(UUID userId) {
    return userRepositoryPort.findById(userId).isPresent();
  }

}
