package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto create(UserCreateRequest userRequest, BinaryContentCreateRequest profileRequest) {
    if (userRepository.existsByUsername(userRequest.username())) {
      throw new IllegalStateException("[Error] 동일한 username");
    }
    if (userRepository.existsByEmail((userRequest.email()))) {
      throw new IllegalStateException("[Error] 동일한 email");
    }

    BinaryContent profile = null;
    if (isValidBinaryContent(profileRequest)) {
      profile = new BinaryContent(
          profileRequest.fileName(),
          profileRequest.file().getSize(),
          profileRequest.contentType()
      );
      binaryContentRepository.save(profile);
    }

    User newUser = userMapper.toEntity(userRequest);
    newUser.updateProfile(profile);

    UserStatus newUserStatus = new UserStatus(newUser);
    newUser.updateUserStatus(newUserStatus);

    User savedUser = userRepository.save(newUser);
    return userMapper.toDto(savedUser);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto read(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다."));
    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserDto> readAll() {
    List<User> users = userRepository.findAll();

    if (users.isEmpty()) {
      throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
    }
    return users.stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest request,
      BinaryContentCreateRequest profileRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalStateException("[Error] user not found"));

    if (request.newUsername() != null && !request.newUsername().equals(user.getUsername())) {
      if (userRepository.existsByUsername(request.newUsername())) {
        throw new IllegalArgumentException(
            "User with username " + request.newUsername() + " already exists");
      }
      user.updateName(request.newUsername());
    }
    if (request.newEmail() != null && !request.newEmail().equals(user.getEmail())) {
      if (userRepository.existsByEmail(request.newEmail())) {
        throw new IllegalArgumentException(
            "User with email " + request.newEmail() + " already exists");
      }
      user.updateEmail(request.newEmail());
    }
    if (request.newPassword() != null && !request.newPassword().isEmpty()) {
      user.updatePassword(request.newPassword());
    }

    if (isValidBinaryContent(profileRequest)) {
      BinaryContent newProfile = new BinaryContent(
          profileRequest.fileName(),
          profileRequest.file().getSize(),
          profileRequest.contentType()
      );
      binaryContentRepository.save(newProfile);

      user.updateProfile(newProfile);
    }

    return userMapper.toDto(user);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalStateException("[Error] 유저가 존재하지 않습니다."));

    if (user.getProfile() != null) {
      binaryContentRepository.delete(user.getProfile());
    }
    userRepository.delete(user);
  }

  private boolean isValidBinaryContent(BinaryContentCreateRequest request) {
    return request != null &&
        request.fileName() != null &&
        !request.fileName().isBlank();
  }
}
