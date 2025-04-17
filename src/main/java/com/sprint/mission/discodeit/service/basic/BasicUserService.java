package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestException;
import com.sprint.mission.discodeit.exception.ResultCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      throw new RestException(ResultCode.BAD_REQUEST);
    }
    if (userRepository.existsByUsername(username)) {
      throw new RestException(ResultCode.BAD_REQUEST);
    }

    BinaryContent profile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent meta = new BinaryContent(fileName, (long) bytes.length, contentType);
          BinaryContent saveMeta = binaryContentRepository.save(meta);

          binaryContentStorage.put(saveMeta.getId(), bytes);

          return saveMeta;
        })
        .orElse(null);

    String password = userCreateRequest.password();
    User user = new User(username, email, password, profile);
    User createdUser = userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(createdUser, now);
    createdUser.updateStatus(userStatus);
    userStatusRepository.save(userStatus);

    return createdUser;
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new RestException(ResultCode.NOT_FOUND));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RestException(ResultCode.NOT_FOUND));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();

    if (userRepository.existsByEmail(newEmail)) {
      throw new RestException(ResultCode.BAD_REQUEST);
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new RestException(ResultCode.BAD_REQUEST);
    }

    BinaryContent newProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          Optional.ofNullable(user.getProfile()).ifPresent(binaryContentRepository::delete);

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent meta = new BinaryContent(fileName, (long) bytes.length, contentType);
          BinaryContent saveMeta = binaryContentRepository.save(meta);

          binaryContentStorage.put(saveMeta.getId(), bytes);
          return saveMeta;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, newProfile);

    return userRepository.save(user);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RestException(ResultCode.NOT_FOUND));

    Optional.ofNullable(user.getProfile())
        .ifPresent(binaryContentRepository::delete);
    userStatusRepository.deleteByUserId(userId);

    userRepository.deleteById(userId);
  }
}
