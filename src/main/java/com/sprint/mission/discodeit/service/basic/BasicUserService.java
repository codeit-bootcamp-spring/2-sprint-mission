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
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final UserMapper userMapper;
  private Logger logger = LoggerFactory.getLogger(this.getClass());


  @Override
  public UserDTO create(CreateUserParam createUserParam, MultipartFile multipartFile) {
    checkDuplicateUsername(createUserParam);
    checkDuplicateEmail(createUserParam);
    BinaryContent binaryContent = createBinaryContentEntity(multipartFile);
    if (binaryContent != null) {
      binaryContentService.create(binaryContent);
    }
    User user = createUserEntity(createUserParam, binaryContent);
    userRepository.save(user);
    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
    userStatusService.create(userStatus);
    return userMapper.toUserDTO(user, userStatus);
  }

  @Override
  public UserDTO find(UUID userId) {
    User findUser = findUserById(userId);
    UserStatus findUserStatus = userStatusService.findByUserId(userId);
    BinaryContentDTO binaryContentDTO =
        findUser.getProfileId() != null ? binaryContentService.find(findUser.getProfileId()) : null;
    return userMapper.toUserDTO(findUser, findUserStatus);
  }

  @Override
  public List<UserDTO> findAll() {
    List<User> users = userRepository.findAll();
    // UserDTO에 isLogin 정보를 담기 위함
    // UserStatusMap을 만들고, User와 해당하는 UserStatus 객체를 userEntityToDTO 메서드에 넘겨줌
    // userEntityDTO에서 userStatus.isLoginUser() 메서드를 실행시켜 isLogin에 대한 정보를 생성
    Map<UUID, UserStatus> userStatusMap = userStatusService.findAll().stream()
        .collect(Collectors.toMap(userStatus -> userStatus.getUserId(),
            userStatus -> userStatus,
            (existing, duplicate) -> duplicate)); // 갱신을 할 경우 중복 key가 발생 -> 중복일 경우 새 데이터로 바꿔줌
    return users.stream()
        .map(user -> userMapper.toUserDTO(user, userStatusMap.get(user.getId())))
        .toList();
  }

  @Override
  public UpdateUserDTO update(UUID userId, UpdateUserParam updateUserParam,
      MultipartFile multipartFile) {
    User findUser = findUserById(userId);
    findUser.updateUserInfo(updateUserParam.newUsername(), updateUserParam.newEmail(),
        updateUserParam.newPassword());
    if (multipartFile != null && !multipartFile.isEmpty()) { // 프로필을 유지하거나 프로필 변경 요청이 있을 때 업데이트
      // 기존 이미지 삭제
      binaryContentService.delete(findUser.getProfileId());
      BinaryContent binaryContent = createBinaryContentEntity(multipartFile);
      BinaryContent createdBinaryContent = binaryContentService.create(binaryContent);
      findUser.updateProfile(createdBinaryContent.getId());
    } else { // 기본 프로필로 변경할 때
      binaryContentService.delete(findUser.getProfileId());
      findUser.updateProfileDefault();
    }
    User user = userRepository.save(findUser);
    UserStatus userStatus = userStatusService.findByUserId(user.getId());
    BinaryContentDTO binaryContentDTO =
        user.getProfileId() != null ? binaryContentService.find(user.getProfileId()) : null;
    return userMapper.toUpdateUserDTO(user, userStatus.isLoginUser(), binaryContentDTO);
  }

  @Override
  public void delete(UUID userId) {
    User user = findUserById(userId);
    userRepository.deleteById(userId);
    // 기본 프로필 ID가 아닐 때만 프로필 파일 삭제
    binaryContentService.delete(user.getProfileId());
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
        .profileId(binaryContent != null ? binaryContent.getId() : null)
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
    if (multipartFile == null || multipartFile.isEmpty()) {
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
