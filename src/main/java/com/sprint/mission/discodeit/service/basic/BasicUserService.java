package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDto save(UserCreateRequest userCreateRequest, MultipartFile profile)
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

    return userMapper.toDto(user);
  }

  @Override
  public List<UserDto> findAllUser() {
    return userRepository.findAllWithProfile().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
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

    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));

    userRepository.delete(user);
  }
}
