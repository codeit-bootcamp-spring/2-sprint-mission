package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserFindDTO;
import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.user.dto.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsError;
import com.sprint.mission.discodeit.core.user.usecase.status.UserStatusService;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepository;
import com.sprint.mission.discodeit.core.user.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserStatusService userStatusService;

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @CustomLogging
  @Override
  public UUID create(UserCreateRequestDTO requestDTO,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO) {

    checkDuplicate(requestDTO.name(), requestDTO.email());

    UUID profileId = makeBinaryContent(binaryContentDTO);
    String hashedPassword = BCrypt.hashpw(requestDTO.password(), BCrypt.gensalt());
    User user = new User(profileId, requestDTO.name(), requestDTO.email(), hashedPassword);
    userRepository.save(user);

    userStatusService.create(user.getId());

    return user.getId();
  }

  @Override
  public UserFindDTO findById(UUID userId) {
    User user = userRepository.findById(userId);

    UserStatus userStatus = userStatusService.findByUserId(userId);
    boolean online = userStatus.isOnline();

    return UserFindDTO.create(user, online);
  }

  @Override
  public List<UserFindDTO> listAllUsers() {
    List<User> userList = userRepository.findAll();

    return userList.stream().map(user -> UserFindDTO.create(
        user,
        userStatusService.findByUserId(user.getId()).isOnline())
    ).toList();
  }

  @CustomLogging
  @Override
  public UUID update(UUID userId, UpdateUserRequestDTO updateUserRequestDTO,
      Optional<BinaryContentCreateRequestDTO> binaryContentDTO) {

    User user = userRepository.findById(userId);
    UUID profileId = user.getProfileId();
    if (profileId != null) {
      binaryContentRepository.delete(profileId);
    }
    UUID newProfileId = makeBinaryContent(binaryContentDTO);

    User update = userRepository.update(user, updateUserRequestDTO, newProfileId);

    return update.getId();
  }

  @CustomLogging
  @Override
  public void delete(UUID userId) {

    User findUser = userRepository.findById(userId);

    Optional.ofNullable(findUser.getProfileId())
        .ifPresent(binaryContentRepository::delete);

    userStatusService.deleteById(findUser.getId());

    userRepository.remove(findUser);

  }

  @Override
  public boolean existsById(UUID userId) {
    return userRepository.existId(userId);
  }

  private UUID makeBinaryContent(Optional<BinaryContentCreateRequestDTO> binaryContentDTO) {
    return binaryContentDTO.map(contentDTO -> {
      String fileName = contentDTO.fileName();
      String contentType = contentDTO.contentType();
      byte[] bytes = contentDTO.bytes();
      long size = bytes.length;

      BinaryContent content = new BinaryContent(fileName, size, contentType, bytes);
      binaryContentRepository.save(content);

      return content.getId();
    }).orElse(null);
  }

  private void checkDuplicate(String name, String email) {
    if (userRepository.existName(name) || userRepository.existEmail(email)) {
      throw new UserAlreadyExistsError("동일한 유저가 존재합니다.");
    }
  }
}
