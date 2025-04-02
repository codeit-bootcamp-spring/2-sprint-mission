package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserFindDTO;
import com.sprint.mission.discodeit.core.user.usecase.status.UserStatusService;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsError;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import com.sprint.mission.discodeit.logging.CustomLogging;
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

  // TODO. 에러 던질 때 import static 로 하기
  @Override
  public UserFindDTO findById(UUID userId) {
    User user = userRepositoryPort.findById(userId)
        .orElseThrow(() -> new UserNotFoundError("유저가 존재하지 않습니다."));
    boolean online = userStatusService.findByUserId(userId).isOnline();
    return UserFindDTO.create(user, online);
  }

  @Override
  public List<UserFindDTO> listAllUsers() {
    List<User> userList = userRepositoryPort.findAll();

    return userList.stream().map(user -> UserFindDTO.create(
        user,
        userStatusService.findByUserId(user.getId()).isOnline())
    ).toList();
  }

  @CustomLogging
  @Override
  public void update(UUID userId, UpdateUserCommand command,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO) {

    User user = userRepositoryPort.findById(command.requestUserId())
        .orElseThrow(() -> new UserNotFoundError("유저가 존재하지 않습니다."));
    UUID profileId = user.getProfileId();
    if (profileId != null) {
      binaryContentRepositoryPort.delete(profileId);
    }
    UUID newProfileId = makeBinaryContent(binaryContentDTO);
    user.update(command.replaceName(), command.replaceEmail(), newProfileId);

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
