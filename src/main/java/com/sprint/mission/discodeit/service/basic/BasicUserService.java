package com.sprint.mission.discodeit.service.basic;

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
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public UserCreateResponse save(UserCreateRequest userCreateRequest, MultipartFile profile)
      throws IOException {
    if (userRepository.findByUsername(userCreateRequest.username()).isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with username %s already exists", userCreateRequest.username()));
    }

    if (userRepository.findByEmail(userCreateRequest.email()).isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with email %s already exists", userCreateRequest.email()));
    }

    BinaryContent binaryContent = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContent = BinaryContent.builder()
          .fileName(profile.getOriginalFilename())
          .contentType(profile.getContentType())
          .bytes(profile.getBytes())
          .size((long) profile.getBytes().length)
          .build();
    }

    User user = new User(
        userCreateRequest.username(),
        userCreateRequest.password(),
        userCreateRequest.email(),
        binaryContent);

    userRepository.save(user);

    UUID profileId = user.getProfile() != null ? user.getProfile().getId() : null;

    return new UserCreateResponse(user.getId(), user.getUsername(), user.getEmail(),
        profileId, user.getCreatedAt());
  }

  @Override
  public FindUserDto findByUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException(
            String.format("User with userId %s not found", userId)));

    UUID profileId = user.getProfile() != null ? user.getProfile().getId() : null;

    FindUserDto findUserDto = new FindUserDto(
        user.getId(), user.getUsername(), user.getEmail(),
        profileId, user.getCreatedAt(), user.getUpdatedAt(),
        userStatus.getLastActiveAt(), userStatus.isLastStatus());

    return findUserDto;
  }

  @Override
  public List<FindUserDto> findAllUser() {
    List<User> userList = userRepository.findAll();

    return userList.stream()
        .map(user -> findByUser(user.getId()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UserUpdateResponse update(UUID userId, UserUpdateRequest userUpdateRequest,
      MultipartFile profile) throws IOException {
    User user = userRepository.findById(userId).
        orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));

    if (userRepository.findByUsername(userUpdateRequest.newUsername())
        .filter(otherUser -> !otherUser.getId().equals(user.getId()))
        .isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with username %s already exists", userUpdateRequest.newUsername()));
    }

    if (userRepository.findByEmail(userUpdateRequest.newEmail())
        .filter(otherUser -> !otherUser.getId().equals(user.getId()))
        .isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with email %s already exists", userUpdateRequest.newEmail()));
    }

    BinaryContent binaryContent = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContent = BinaryContent.builder()
          .fileName(profile.getOriginalFilename())
          .contentType(profile.getContentType())
          .bytes(profile.getBytes())
          .size((long) profile.getBytes().length)
          .build();
    }

    user.updateUsername(userUpdateRequest.newUsername());
    user.updatePassword(userUpdateRequest.newPassword());
    user.updateEmail(userUpdateRequest.newEmail());
    user.updateProfile(binaryContent);

    UUID profileId = user.getProfile() != null ? user.getProfile().getId() : null;

    return new UserUpdateResponse(user.getId(), user.getUsername(), user.getEmail(),
        profileId, user.getCreatedAt(), user.getUpdatedAt());
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));

    userRepository.delete(user);
  }
}
