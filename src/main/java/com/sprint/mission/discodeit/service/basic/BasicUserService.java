package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryData;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.BinaryDataRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryDataRepository binaryDataRepository;

  @Override
  public User save(SaveUserRequestDto saveUserParamDto,
      Optional<SaveBinaryContentRequestDto> saveBinaryContentRequestDto) {
    if (userRepository.findUserByUsername(saveUserParamDto.username()).isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with username %s already exists", saveUserParamDto.username()));
    }

    if (userRepository.findUserByEmail(saveUserParamDto.email()).isPresent()) {
      throw new IllegalArgumentException(
          String.format("User with email %s already exists", saveUserParamDto.email()));
    }

    UUID profileId = saveBinaryContentRequestDto
        .map(profile -> {
          String fileName = profile.fileName();
          String contentType = profile.contentType();
          byte[] fileData = profile.fileData();
          BinaryData binaryData = binaryDataRepository.save(new BinaryData(fileData));
          BinaryContent binaryContent = new BinaryContent(binaryData.getId(), fileName,
              contentType);
          binaryContentRepository.save(binaryContent);
          return binaryContent.getId();
        })
        .orElse(null);

    User user = new User(
        saveUserParamDto.username(), saveUserParamDto.password(),
        saveUserParamDto.nickname(), saveUserParamDto.email(),
        profileId);
    userRepository.save(user);
    UserStatus userStatus = UserStatus.builder()
        .userUUID(user.getId())
        .build();
    userStatusRepository.save(userStatus);
    return user;
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
        user.getProfile(), user.getCreatedAt(),
        user.getUpdatedAt(), userStatus.getLastLoginTime(),
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
  public void update(UUID userId, UpdateUserRequestDto updateUserDto,
      Optional<SaveBinaryContentRequestDto> saveBinaryContentRequestDto) {
    User user = userRepository.findUserById(userId).
        orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));

    user.updateNickname(updateUserDto.nickname());
    UUID profileId = saveBinaryContentRequestDto
        .map(profile -> {
          String fileName = profile.fileName();
          String contentType = profile.contentType();
          byte[] fileData = profile.fileData();
          BinaryData binaryData = binaryDataRepository.save(new BinaryData(fileData));
          BinaryContent binaryContent = new BinaryContent(binaryData.getId(), fileName,
              contentType);
          binaryContentRepository.save(binaryContent);
          return binaryContent.getId();
        })
        .orElse(null);
    user.updateProfile(profileId);
    userRepository.save(user);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findUserById(userId)
        .orElseThrow(
            () -> new NoSuchElementException(String.format("User with id %s not found", userId)));
    if (Objects.nonNull(user.getProfile())) {
      binaryContentRepository.delete(user.getProfile());
    }
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(
            () -> new NoSuchElementException(
                String.format("UserStatus with userId %s not found", userId)));

    userStatusRepository.delete(userStatus.getId());
    userRepository.delete(user.getId());
  }
}
