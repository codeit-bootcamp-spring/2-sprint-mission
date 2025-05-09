package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.user.CreateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.CreateUserResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.file.FileReadException;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.util.MaskingUtil;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final UserMapper userMapper;
  private final UserStatusMapper userStatusMapper;
  private final BinaryContentStorage binaryContentStorage;


  @Override
  @Transactional
  public CreateUserResult create(CreateUserCommand createUserCommand, MultipartFile multipartFile) {
    checkDuplicateUsername(createUserCommand);
    checkDuplicateEmail(createUserCommand);

    BinaryContent binaryContent = createBinaryContentEntity(multipartFile);
    if (binaryContent != null) {
      // 파일 저장 <-> 메타데이터 저장 책임 분리가 필요하다고 생각하여,
      // BinaryContentService에서 BinaryContentStorage를 호출하여 함께 저장하는 구조가 아닌, 상위 서비스에서 두 기능을 조합하여 호출하는 구조로 설계함.
      CreateBinaryContentResult createBinaryContentResult = binaryContentService.create(
          binaryContent);
      try {
        binaryContentStorage.put(createBinaryContentResult.id(), multipartFile.getBytes());
      } catch (IOException e) {
        log.error("User create failed: multipartFile read failed (filename: {})",
            multipartFile.getOriginalFilename());
        throw new FileReadException(Map.of("contentType", multipartFile.getContentType(),
            "size", multipartFile.getSize(),
            "filename", multipartFile.getOriginalFilename()));
      }
    }

    User user = createUserEntity(createUserCommand, binaryContent);
    userRepository.save(user);

    // GenerationType.UUID의 경우, save()만 해줘도 User의 id를 DB가 아닌 자바에서 직접 생성
    // IDENTITY와 다르게 flush()를 안해줘도 user.getId()로 접근이 가능하다.
    UserStatus userStatus = new UserStatus(user, Instant.now());
    userStatusService.create(userStatusMapper.toCreateUserStatusCommand(userStatus));

    return userMapper.toCreateUserResult(user);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "user", key = "#p0")
  public FindUserResult find(UUID userId) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
    return userMapper.toFindUserResult(findUser);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "allUsers")
  public List<FindUserResult> findAll() {
    List<User> users = userRepository.findAllFetch();

    return users.stream()
        .map(user -> userMapper.toFindUserResult(user))
        .toList();
  }

  @Override
  @Transactional
  @CachePut(value = "user", key = "#p0")
  @CacheEvict(value = "allUsers", allEntries = true)
  public UpdateUserResult update(UUID userId, UpdateUserCommand updateUserCommand,
      MultipartFile multipartFile) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("User update failed: user not found (userId: {})", userId);
          return new UserNotFoundException(Map.of("userId", userId));
        });

    findUser.updateUserInfo(updateUserCommand.newUsername(), updateUserCommand.newEmail(),
        updateUserCommand.newPassword());
    if (multipartFile != null && !multipartFile.isEmpty()) { // 프로필을 유지하거나 프로필 변경 요청이 있을 때 업데이트
      // 기존 이미지 삭제 (기존 프로필 이미지가 있는 경우에만)
      if (findUser.getProfile() != null) {
        binaryContentService.delete(findUser.getProfile().getId());
        binaryContentStorage.delete(findUser.getProfile().getId());
      }
      BinaryContent binaryContent = createBinaryContentEntity(multipartFile);
      CreateBinaryContentResult createBinaryContentResult = binaryContentService.create(
          binaryContent);
      try {
        binaryContentStorage.put(createBinaryContentResult.id(), multipartFile.getBytes());
      } catch (IOException e) {
        log.error("User update failed: multipartFile read failed (filename: {})",
            multipartFile.getOriginalFilename());
        throw new FileReadException(Map.of("contentType", multipartFile.getContentType(),
            "size", multipartFile.getSize(),
            "filename", multipartFile.getOriginalFilename()));
      }
      findUser.updateProfile(binaryContent);
    } else { // 기본 프로필로 변경할 때
      binaryContentService.delete(findUser.getProfile().getId());
      findUser.updateProfileDefault();
    }
    // 변경감지로 User는 save() 안해줘도 commit 시점에 자동으로 save()됨
    return userMapper.toUpdateUserResult(findUser);
  }


  @Override
  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "user", key = "#p0"),
      @CacheEvict(value = "allUsers", allEntries = true)
  })
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("User delete failed: user not found (userId: {})", userId);
          return new UserNotFoundException(Map.of("userId", userId));
        });
    userRepository.deleteById(userId);
    if (user.getProfile() != null) {
      binaryContentService.delete(user.getProfile().getId());
      binaryContentStorage.delete(user.getProfile().getId());
    }
    userStatusService.deleteByUserId(userId);
  }


  private void checkDuplicateUsername(CreateUserCommand createUserCommand) {
    if (userRepository.existsByUsername(createUserCommand.username())) {
      String maskedUsername = MaskingUtil.maskUsername(createUserCommand.username());
      log.warn("User create failed: duplicate username (username: {})", maskedUsername);
      throw new DuplicateUsernameException(Map.of("username", maskedUsername));
    }
  }

  private void checkDuplicateEmail(CreateUserCommand createUserCommand) {
    if (userRepository.existsByEmail(createUserCommand.email())) {
      String maskedEmail = MaskingUtil.maskEmail(createUserCommand.email());
      log.warn("User create failed: duplicate email (email: {})", maskedEmail);
      throw new DuplicateEmailException(Map.of("email", maskedEmail));
    }
  }

  // 비밀번호를 해싱해서 넣어주는 비즈니스 로직이 있으므로 Mapper가 아닌 Service에 위치
  private User createUserEntity(CreateUserCommand createUserCommand, BinaryContent binaryContent) {
    return User.builder()
        .email(createUserCommand.email())
        .profile(binaryContent)
        .password(BCrypt.hashpw(createUserCommand.password(), BCrypt.gensalt()))
        .username(createUserCommand.username())
        .build();
  }

  private BinaryContent createBinaryContentEntity(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty() || multipartFile.getSize() == 0) {
      return null;  // 파일이 없으면 BinaryContent를 생성하지 않음
    }
    return BinaryContent.builder()
        .contentType(multipartFile.getContentType())
        .size(multipartFile.getSize())
        .filename(multipartFile.getOriginalFilename())
        .build();
  }
}
