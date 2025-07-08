package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  @Override
  public UserDto createUser(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = request.username();
    String email = request.email();
    String rawPassword = request.password();
    String encodedPassword = passwordEncoder.encode(rawPassword);

    checkUserEmailExists(request.email());
    checkUserUsernameExists(request.username());

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          BinaryContent binaryContent = BinaryContent.create(fileName, (long) bytes.length,
              contentType);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return createdBinaryContent;
        })
        .orElse(null);

    User user = User.create(username, email, encodedPassword, nullableProfile);
    User createdUser = userRepository.save(user);

    return userMapper.toDto(createdUser);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasRole('USER')")
  @Override
  public UserDto findById(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> UserNotFoundException.byId(userId));
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasRole('USER')")
  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @PreAuthorize("hasRole('ADMIN') or @authorizationChecker.isSameUser(#userId, authentication)")
  @Override
  public UserDto updateUser(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.byId(userId));

    String newUsername = request.newUsername();
    String newEmail = request.newEmail();
    String rawNewPassword = request.newPassword();
    String encodedNewPassword = null;
    if (rawNewPassword != null) {
      encodedNewPassword = passwordEncoder.encode(rawNewPassword);
    }

    checkUserEmailExists(request.newEmail());
    checkUserUsernameExists(request.newUsername());

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfile())
              .map(BinaryContent::getId)
              .ifPresent(id -> {
                binaryContentStorage.deleteById(id);
                binaryContentRepository.deleteById(id);
              });

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          BinaryContent binaryContent = BinaryContent.create(fileName, (long) bytes.length,
              contentType);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return createdBinaryContent;
        })
        .orElse(null);

    user.update(newUsername, newEmail, encodedNewPassword, nullableProfile);

    return userMapper.toDto(user);
  }

  @Transactional
  @PreAuthorize("hasRole('ADMIN') or @authorizationChecker.isSameUser(#userId, authentication)")
  @Override
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.byId(userId));

    if (user.getProfile() != null && user.getProfile().getId() != null) {
      UUID profileId = user.getProfile().getId();
      binaryContentStorage.deleteById(profileId);
      binaryContentRepository.deleteById(profileId);
    }

    userRepository.deleteById(userId);
  }


  /****************************
   * Validation check
   ****************************/
  private void checkUserEmailExists(String email) {
    if (userRepository.existsByEmail(email)) {
      throw UserAlreadyExistException.byEmail(email);
    }
  }

  private void checkUserUsernameExists(String username) {
    if (userRepository.existsByUsername(username)) {
      throw UserAlreadyExistException.byUsername(username);
    }
  }

}
