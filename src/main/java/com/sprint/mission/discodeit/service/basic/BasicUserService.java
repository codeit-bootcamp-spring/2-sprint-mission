package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
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
  public UserResult register(UserCreateRequest userRequest, MultipartFile profileImage) {
    validateDuplicateEmail(userRequest.email());
    validateDuplicateUserName(userRequest.name());

    UUID binaryContentId = null;
    if (profileImage != null) {
      BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(profileImage));
      binaryContentId = binaryContent.getId();
    }

    User savedUser = userRepository.save(new User(
            userRequest.name(),
            userRequest.email(),
            userRequest.password(),
            binaryContentId
        )
    );
    UserStatus userStatus = userStatusRepository.save(new UserStatus(savedUser.getId()));

    return UserResult.fromEntity(savedUser, userStatus.isLogin(Instant.now()));
  }

  @Override
  public UserResult getById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저Id를 가진 UserStatus가 없습니다."));

    return UserResult.fromEntity(user, userStatus.isLogin(Instant.now()));
  }

  @Override
  public UserResult getByName(String name) {
    User user = userRepository.findByName(name)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("해당 유저Id를 가진 UserStatus가 없습니다."));

    return UserResult.fromEntity(user, userStatus.isLogin(Instant.now()));
  }

  @Override
  public List<UserResult> getAll() {
    return userRepository.findAll()
        .stream()
        .map(user -> {
          UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
              .orElseThrow(() -> new IllegalArgumentException("해당 유저Id를 가진 UserStatus가 없습니다."));

          return UserResult.fromEntity(user, userStatus.isLogin(Instant.now()));
        })
        .toList();
  }

  @Override
  public UserResult getByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new IllegalArgumentException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new IllegalArgumentException("해당 유저Id를 가진 UserStatus가 없습니다."));

    return UserResult.fromEntity(user, userStatus.isLogin(Instant.now()));
  }

  @Override
  public List<UserResult> getAllByIds(List<UUID> userIds) {
    return userIds
        .stream()
        .map(this::getById)
        .toList();
  }

  @Override
  public UserResult updateName(UUID userId, String name) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));
    user.updateName(name);
    User savedUser = userRepository.save(user);

    UserStatus userStatus = userStatusRepository.findByUserId(savedUser.getId())
        .orElseThrow(() -> new IllegalArgumentException("해당 유저Id를 가진 UserStatus가 없습니다."));

    return UserResult.fromEntity(savedUser, userStatus.isLogin(Instant.now()));
  }

  @Override
  public UserResult updateProfileImage(UUID userId, MultipartFile profileImage) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));
    binaryContentRepository.delete(user.getProfileId());

    UUID binaryContentId = null;
    if (profileImage != null) {
      BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(profileImage));
      binaryContentId = binaryContent.getId();
    }

    user.updateProfileImage(binaryContentId);
    User savedUser = userRepository.save(user);

    UserStatus userStatus = userStatusRepository.findByUserId(savedUser.getId())
        .orElseThrow(() -> new IllegalArgumentException("해당 유저Id를 가진 UserStatus가 없습니다."));

    return UserResult.fromEntity(savedUser, userStatus.isLogin(Instant.now()));
  }

  @Override
  public void delete(UUID userId) {
    userRepository.delete(userId);

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

    userStatusRepository.delete(userStatus.getId());
  }

  private void validateDuplicateUserName(String name) {
    boolean isDuplicate = userRepository.findByName(name)
        .isPresent();

    if (isDuplicate) {
      throw new IllegalArgumentException("이미 존재하는 이름 입니다");
    }
  }

  private void validateDuplicateEmail(String requestEmail) {
    boolean isDuplicate = userRepository.findAll()
        .stream()
        .anyMatch(existingUser -> existingUser.isSameEmail(requestEmail));

    if (isDuplicate) {
      throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
    }
  }
}
