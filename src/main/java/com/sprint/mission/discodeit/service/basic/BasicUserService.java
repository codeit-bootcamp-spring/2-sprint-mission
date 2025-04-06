package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public UserCreateResponse save(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> saveBinaryContentRequestDto) {
    if (userRepository.findUserByUsername(userCreateRequest.username()).isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with username %s already exists", userCreateRequest.username()));
    }

    if (userRepository.findUserByEmail(userCreateRequest.email()).isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with email %s already exists", userCreateRequest.email()));
    }

    UUID profileId = saveBinaryContentRequestDto
        .map(profile -> {
          String fileName = profile.fileName();
          String contentType = profile.contentType();
          byte[] bytes = profile.bytes();
          BinaryContent binaryContent = BinaryContent.builder()
              .fileName(fileName)
              .contentType(contentType)
              .bytes(bytes)
              .size((long) bytes.length)
              .build();
          binaryContentRepository.save(binaryContent);
          return binaryContent.getId();
        })
        .orElse(null);

    User user = new User(
        userCreateRequest.username(), userCreateRequest.password(),
        userCreateRequest.email(), profileId);
    userRepository.save(user);
    UserStatus userStatus = UserStatus.builder()
        .userId(user.getId())
        .build();
    userStatusRepository.save(userStatus);
    return new UserCreateResponse(user.getId(), user.getUsername(), user.getEmail(),
        user.getProfileId(), user.getCreatedAt());
  }

  @Override
  public FindUserDto findByUser(UUID userId) {
    User user = userRepository.findUserById(userId)
        .orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException(
            String.format("User with userId %s not found", userId)));

    FindUserDto findUserDto = new FindUserDto(
        user.getId(), user.getUsername(), user.getEmail(),
        user.getProfileId(), user.getCreatedAt(),
        user.getUpdatedAt(), userStatus.getLastActiveAt(),
        userStatus.isLastStatus());

    return findUserDto;
  }

  @Override
  public List<FindUserDto> findAllUser() {
    List<User> userList = userRepository.findAllUser();

    return userList.stream()
        .map(user -> findByUser(user.getId()))
        .collect(Collectors.toList());
  }

  @Override
  public UserUpdateResponse update(UUID userId, UserUpdateRequest updateUserDto,
      Optional<BinaryContentCreateRequest> saveBinaryContentRequestDto) {
    User user = userRepository.findUserById(userId).
        orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));

    if (userRepository.findUserByUsername(updateUserDto.newUsername())
        .filter(otherUser -> !otherUser.getId().equals(user.getId()))
        .isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with username %s already exists", updateUserDto.newUsername()));
    }

    if (userRepository.findUserByEmail(updateUserDto.newEmail())
        .filter(otherUser -> !otherUser.getId().equals(user.getId()))
        .isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with email %s already exists", updateUserDto.newEmail()));
    }

    user.updateUsername(updateUserDto.newUsername());
    user.updatePassword(updateUserDto.newPassword());
    user.updateEmail(updateUserDto.newEmail());

    UUID profileId = saveBinaryContentRequestDto
        .map(profile -> {
          String fileName = profile.fileName();
          String contentType = profile.contentType();
          byte[] fileData = profile.bytes();
          BinaryContent binaryContent = BinaryContent.builder()
              .fileName(fileName)
              .contentType(contentType)
              .bytes(fileData)
              .size((long) fileData.length)
              .build();
          binaryContentRepository.save(binaryContent);
          return binaryContent.getId();
        })
        .orElse(null);
    user.updateProfile(profileId);
    userRepository.save(user);
    return new UserUpdateResponse(user.getId(), user.getUsername(), user.getEmail(),
        user.getProfileId(), user.getCreatedAt(), user.getUpdatedAt());
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findUserById(userId)
        .orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(
            () -> new NoSuchElementException(
                String.format("UserStatus with userId %s not found", userId)));

    userStatusRepository.delete(userStatus.getId());
    userRepository.delete(user.getId());
  }
}
