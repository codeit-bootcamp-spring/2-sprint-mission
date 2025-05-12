package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;

  @Override
  public UserResponse create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest profileCreateRequest) {
    userRepository.findByUsername(userCreateRequest.username())
        .ifPresent(u -> {
          throw new DuplicateUsernameException();
        });
    userRepository.findByEmail(userCreateRequest.email())
        .ifPresent(u -> {
          throw new DuplicateEmailException();
        });

    BinaryContent profile = (profileCreateRequest != null)
        ? binaryContentService.create(profileCreateRequest)
        : null;

    User user = new User(userCreateRequest.username(),
        userCreateRequest.email(), userCreateRequest.password(), profile);

    userRepository.save(user);

    userStatusService.create(new UserStatusCreateRequest(user.getId()));

    return userMapper.toResponse(user);
  }

  @Override
  public UserResponse find(UUID userId) {
    User user = getUserBy(userId);

    return userMapper.toResponse(user);
  }

  @Override
  public List<UserResponse> findAll() {
    return userRepository.findAll().stream()
        .map(userMapper::toResponse).toList();
  }

  @Override
  public UserResponse update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest profileCreateRequest) {
    User user = getUserBy(userId);

    if (userUpdateRequest.newUsername() != null) {
      validateUsernameDuplicate(userUpdateRequest.newUsername());
    }

    if (userUpdateRequest.newEmail() != null) {
      validateEmailDuplicate(userUpdateRequest.newEmail());
    }

    BinaryContent newProfile = null;
    if (profileCreateRequest != null) {
      if (user.getProfile() != null) {
        binaryContentService.delete(user.getProfile());
      }
      newProfile = binaryContentService.create(profileCreateRequest);
    }

    user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(), newProfile);
    userRepository.save(user);

    return userMapper.toResponse(user);
  }

  @Override
  public void delete(UUID userId) {
    userRepository.deleteById(userId);
  }

  private User getUserBy(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException());
  }


  private void validateEmailDuplicate(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateEmailException();
    }
  }

  private void validateUsernameDuplicate(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new DuplicateUsernameException();
    }
  }

  private boolean isOnline(UUID userId) {
    return userStatusService.findByUserId(userId).isOnline();
  }
}
