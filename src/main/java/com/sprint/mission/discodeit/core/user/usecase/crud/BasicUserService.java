package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.UserFindDTO;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.core.user.usecase.crud.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.status.UserStatusService;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsError;
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
    checkDuplicate(command.name(), command.email());

    String hashedPassword = BCrypt.hashpw(command.password(), BCrypt.gensalt());
    if (binaryContentDTO.isPresent()) {
      profileId = makeBinaryContent(binaryContentDTO);
    }

    User user = User.create(command.name(), command.email(), hashedPassword, profileId);
    userRepositoryPort.save(user);
    userStatusService.create(user.getId());
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

  private void checkDuplicate(String name, String email) {
    if (userRepositoryPort.existName(name) || userRepositoryPort.existEmail(email)) {
      throw new UserAlreadyExistsError("동일한 유저가 존재합니다.");
    }
  }

  @Override
  public UserFindDTO findById(UUID userId) {
    User user = userRepositoryPort.findById(userId);
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

//  @CustomLogging
//  @Override
//  public UUID update(UUID userId, UpdateUserRequestDTO updateUserRequestDTO,
//      Optional<BinaryContentCreateRequestDTO> binaryContentDTO) {
//
//    User user = userRepositoryPort.findById(userId);
//    UUID profileId = user.getProfileId();
//    if (profileId != null) {
//      binaryContentRepositoryPort.delete(profileId);
//    }
//    UUID newProfileId = makeBinaryContent(binaryContentDTO);
//
//    User update = userRepositoryPort.update(user, updateUserRequestDTO, newProfileId);
//
//    return update.getId();
//  }

  @CustomLogging
  @Override
  public void delete(UUID userId) {
    User findUser = userRepositoryPort.findById(userId);

    Optional.ofNullable(findUser.getProfileId())
        .ifPresent(binaryContentRepositoryPort::delete);

    userStatusService.deleteById(findUser.getId());
    userRepositoryPort.remove(findUser);
  }

  @Override
  public boolean existsById(UUID userId) {
    return userRepositoryPort.existId(userId);
  }

}
