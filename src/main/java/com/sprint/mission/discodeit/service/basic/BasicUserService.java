package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final UserMapper userMapper;
  private Logger logger = LoggerFactory.getLogger(this.getClass());


  @Override
  @Transactional
  public UserDTO create(CreateUserParam createUserParam, MultipartFile multipartFile) {
    checkDuplicateUsername(createUserParam);
    checkDuplicateEmail(createUserParam);

    BinaryContent binaryContent = createBinaryContentEntity(multipartFile);
    if (binaryContent != null) {
      binaryContentService.create(binaryContent);
    }

    User user = createUserEntity(createUserParam, binaryContent);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user, Instant.now());
    userStatusService.create(userStatus);

    return userMapper.toUserDTO(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDTO find(UUID userId) {
    User findUser = findUserById(userId);
    return userMapper.toUserDTO(findUser);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDTO> findAll() {
    List<User> users = userRepository.findAll();

    return users.stream()
        .map(user -> userMapper.toUserDTO(user))
        .toList();
  }

  @Override
  @Transactional
  public UpdateUserDTO update(UUID userId, UpdateUserParam updateUserParam,
      MultipartFile multipartFile) {
    User findUser = findUserById(userId);
    findUser.updateUserInfo(updateUserParam.newUsername(), updateUserParam.newEmail(),
        updateUserParam.newPassword());
    if (multipartFile != null && !multipartFile.isEmpty()) { // 프로필을 유지하거나 프로필 변경 요청이 있을 때 업데이트
      // 기존 이미지 삭제
      binaryContentService.delete(findUser.getProfile().getId());

      BinaryContent binaryContent = createBinaryContentEntity(multipartFile);
      BinaryContent createdBinaryContent = binaryContentService.create(binaryContent);

      findUser.updateProfile(createdBinaryContent);
    } else { // 기본 프로필로 변경할 때
      binaryContentService.delete(findUser.getProfile().getId());
      findUser.updateProfileDefault();
    }
    // 변경감지로 User는 save() 안해줘도 commit 시점에 자동으로 save()됨
    return userMapper.toUpdateUserDTO(findUser);
  }


  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = findUserById(userId);
    userRepository.deleteById(userId);
    if (user.getProfile() != null) {
      binaryContentService.delete(user.getProfile().getId());
    }
    userStatusService.deleteByUserId(userId);
  }


  private void checkDuplicateUsername(CreateUserParam createUserParam) {
    if (userRepository.existsByUsername(createUserParam.username())) {
      throw RestExceptions.DUPLICATE_USERNAME;
    }
  }

  private void checkDuplicateEmail(CreateUserParam createUserParam) {
    if (userRepository.existsByEmail(createUserParam.email())) {
      throw RestExceptions.DUPLICATE_EMAIL;
    }
  }

  // 비밀번호를 해싱해서 넣어주는 비즈니스 로직이 있으므로 Mapper가 아닌 Service에 위치
  private User createUserEntity(CreateUserParam createUserParam, BinaryContent binaryContent) {
    return User.builder()
        .email(createUserParam.email())
        .profile(binaryContent)
        .password(BCrypt.hashpw(createUserParam.password(), BCrypt.gensalt()))
        .username(createUserParam.username())
        .build();
  }


  private User findUserById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> {
          logger.error("유저 찾기 실패: {}", userId);
          return RestExceptions.USER_NOT_FOUND;
        });
  }

  private BinaryContent createBinaryContentEntity(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty() || multipartFile.getSize() == 0) {
      return null;  // 파일이 없으면 BinaryContent를 생성하지 않음
    }
    try {
      return BinaryContent.builder()
          .contentType(multipartFile.getContentType())
          .bytes(multipartFile.getBytes())
          .size(multipartFile.getSize())
          .filename(multipartFile.getOriginalFilename())
          .build();
    } catch (IOException e) {
      logger.error("파일 읽기 실패: {}", multipartFile.getOriginalFilename(), e);
      throw RestExceptions.FILE_READ_ERROR;
    }
  }


}
