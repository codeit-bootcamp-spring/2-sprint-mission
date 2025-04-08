package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentService binaryContentService;

  @Override
  public UserDto create(UserCreateRequest userRequest, BinaryContentCreateRequest profileRequest) {
    UUID profileImageKey = null;
    if (userRepository.existsByName(userRequest.username())) {
      throw new IllegalStateException("[Error] 동일한 name");
    }
    if (userRepository.existsByEmail(userRequest.email())) {
      throw new IllegalStateException("[Error] 동일한 email");
    }
    if (isValidBinaryContent(profileRequest)) {
      profileImageKey = binaryContentService.create(profileRequest).getId();
    }

    User user = new User(userRequest.username(), userRequest.password(), userRequest.email(),
        profileImageKey);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.EPOCH);
    userStatusRepository.save(userStatus);

    return new UserDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
        user.getEmail(), user.getProfileId(), userStatus.isOnline());
  }

  @Override
  public UserDto read(UUID userKey) {
    User user = userRepository.findByKey(userKey);
    UserStatus userStatus = userStatusRepository.findByUserKey(user.getId());

    if (user.getId() == null) {
      throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
    }

    return new UserDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
        user.getEmail(), user.getProfileId(), userStatus.isOnline());
  }

  @Override
  public List<UserDto> readAll() {
    List<User> users = userRepository.findAll();
    if (users.isEmpty()) {
      throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
    }
    return users.stream().map(user -> {
          UserStatus userStatus = userStatusRepository.findByUserKey(user.getId());
          return new UserDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
              user.getEmail(), user.getProfileId(), userStatus.isOnline());
        })
        .toList();
  }

  @Override
  public User update(UUID userKey, UserUpdateRequest request,
      BinaryContentCreateRequest profileRequest) {
    User user = userRepository.findByKey(userKey);
    UUID updateProfileKey;
    if (user == null) {
      throw new IllegalStateException("[Error] user not found");
    }

    String newUsername = request.newUsername();
    String newEmail = request.newEmail();

    if (request.newUsername() != null) {
      if (userRepository.existsByName(newUsername)) {
        throw new IllegalArgumentException("User with username " + newUsername + " already exists");
      }
      user.updateName(request.newUsername());
    }
    if (newEmail != null && !newEmail.isEmpty()) {
      if (userRepository.existsByEmail(newEmail)) {
        throw new IllegalArgumentException("User with email " + newEmail + " already exists");
      }
      user.updateEmail(request.newEmail());
    }
    if (request.newPassword() != null && !request.newPassword().isEmpty()) {
      user.updatePwd(request.newPassword());
    }

    if (isValidBinaryContent(profileRequest)) {
      UUID currentProfileId = user.getProfileId();
      if (currentProfileId != null) {
        binaryContentRepository.delete(currentProfileId);
      }
      updateProfileKey = binaryContentService.create(profileRequest).getId();
      user.updateProfileId(updateProfileKey);
    }

    return userRepository.save(user);
  }

  @Override
  public void delete(UUID userKey) {
    User user = userRepository.findByKey(userKey);

    if (user == null) {
      throw new IllegalStateException("[Error] 유저가 존재하지 않습니다.");
    }
    if (user.getProfileId() != null) {
      binaryContentRepository.delete(user.getProfileId());
    }
    userStatusService.deleteByUserKey(userKey);
    userRepository.delete(userKey);
  }

  private boolean isValidBinaryContent(BinaryContentCreateRequest request) {
    return request != null &&
        request.bytes() != null &&
        request.bytes().length > 0 &&
        request.fileName() != null &&
        !request.fileName().isBlank();
  }
}
