package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public UserCreateResponse create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest profileCreateRequest) {
    validateUsernameDuplicate(userCreateRequest.username());
    validateEmailDuplicate(userCreateRequest.email());

    UUID profileId = (profileCreateRequest != null)
        ? binaryContentService.create(profileCreateRequest).getId()
        : null;

    User user = new User(userCreateRequest.username(),
        userCreateRequest.email(), userCreateRequest.password(), profileId);
    userRepository.save(user);

    userStatusService.create(new UserStatusCreateRequest(user.getId()));

    return UserCreateResponse.fromEntity(user);
  }

  @Override
  public UserResponse find(UUID userId) {
    User user = getUserBy(userId);

    return UserResponse.fromEntity(user, isOnline(userId));
  }

  @Override
  public List<UserResponse> findAll() {
    return userRepository.findAll().stream()
        .map(user -> UserResponse.fromEntity(user, isOnline(user.getId())))
        .toList();
  }

  @Override
  public UserUpdateResponse update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest profileCreateRequest) {
    User user = getUserBy(userId);

    if (userUpdateRequest.newUsername() != null) {
      validateUsernameDuplicate(userUpdateRequest.newUsername());
    }

    if (userUpdateRequest.newEmail() != null) {
      validateEmailDuplicate(userUpdateRequest.newEmail());
    }

    UUID newProfileId = null;
    if (profileCreateRequest != null) {
      if (user.getProfile() != null) {
        binaryContentService.delete(user.getProfile());
      }
      newProfileId = binaryContentService.create(profileCreateRequest).getId();
    }

    user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(), newProfileId);
    userRepository.save(user);

    return UserUpdateResponse.fromEntity(user);
  }

  @Override
  public void delete(UUID userId) {
    User user = getUserBy(userId);

    if (user.hasProfile()) {
      binaryContentRepository.deleteById(user.getProfile());
    }

    userStatusService.deleteByUserId(userId);

    userRepository.deleteById(userId);
  }

  private User getUserBy(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 유저 없음"));
  }


  private void validateEmailDuplicate(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateResourceException("동일 newEmail 이미 존재함");
    }
  }

  private void validateUsernameDuplicate(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new DuplicateResourceException("동일 newUsername 이미 존재함");
    }
  }

  private boolean isOnline(UUID userId) {
    return userStatusService.findByUserId(userId).isOnline();
  }
}
