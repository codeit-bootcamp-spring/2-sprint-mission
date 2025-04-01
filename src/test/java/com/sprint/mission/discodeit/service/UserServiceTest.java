package com.sprint.mission.discodeit.service;

import static com.sprint.mission.discodeit.util.mock.file.MockFile.createMockImageFile;
import static com.sprint.mission.discodeit.util.mock.user.MockUser.createMockUserRequest;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class UserServiceTest {

  private UserService userService;
  private UserResult setUpUser;
  private UserRepository userRepository;
  private UserStatusRepository userStatusRepository;
  private BinaryContentRepository binaryContentRepository;

  @BeforeEach
  void setUp() {
    userRepository = new JCFUserRepository();
    userStatusRepository = new JCFUserStatusRepository();
    binaryContentRepository = new JCFBinaryContentRepository();
    userService = new BasicUserService(userRepository, userStatusRepository,
        binaryContentRepository);

    UserCreateRequest mockUserRequest = createMockUserRequest(LOGIN_USER.getName(),
        LOGIN_USER.getEmail());
    setUpUser = userService.register(mockUserRequest, null);
  }

  @DisplayName("이미 등록된 유저 이메일로 등록한다면 예외를 반환합니다")
  @Test
  void registerDuplicateEmail() {
    UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
        LOGIN_USER.getEmail());

    assertThatThrownBy(() -> userService.register(mockUserRequest, null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("이미 등록된 유저 이름으로 등록한다면 예외를 반환합니다.")
  @Test
  void registerDuplicateUserName() {
    UserCreateRequest mockUserRequest = createMockUserRequest(LOGIN_USER.getName(),
        OTHER_USER.getEmail());

    assertThatThrownBy(() -> userService.register(mockUserRequest, null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("프로필 사진을 저장하면 파일 저장 경로를 반환합니다.")
  @Test
  void register() throws IOException {
    MultipartFile imageFile = createMockImageFile("dog.jpg");
    UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
        OTHER_USER.getEmail());

    UserResult user = userService.register(mockUserRequest, imageFile);
    BinaryContent binaryContent = binaryContentRepository.findById(user.profileId()).get();

    assertThat(imageFile.getBytes()).isEqualTo(binaryContent.getBytes());
  }

  @DisplayName("프로필 사진 저장을 선택하지 않았을 때 프로필 아이디가 null을 반환합니다.")
  @Test
  void registerException() {
    UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
        OTHER_USER.getEmail());

    UserResult user = userService.register(mockUserRequest, null);
    assertThat(user.profileId()).isNull();
  }

  @DisplayName("처음 등록한 유저의 로그인 상태는 false를 반환한다.")
  @Test
  void registerValidateUserStatus() {
    UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
        OTHER_USER.getEmail());
    UserResult user = userService.register(mockUserRequest, null);
    assertThat(user.isLogin()).isFalse();
  }

  @DisplayName("유저 조회 시 등록되지않은 ID로 조회한다면 예외를 반환합니다.")
  @Test
  void getByIdThrowsExceptionWhenUserNotFound() {
    UUID randomUUID = UUID.randomUUID();

    assertThatThrownBy(() -> userService.getById(randomUUID))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("유저 ID로 조회시 해당 Id를 가진 유저를 반환합니다.")
  @Test
  void getByIdReturnsUser() {
    UserResult userResult = userService.getById(setUpUser.id());

    assertThat(userResult.id()).isEqualTo(setUpUser.id());
  }

  @DisplayName("유저 이름으로 조회시 해당 이름을 가진 유저를 반환합니다.")
  @Test
  void getByNameReturnsUser() {
    UserResult user = userService.getByName(LOGIN_USER.getName());

    assertThat(user.name()).isEqualTo(LOGIN_USER.getName());
  }

  @DisplayName("유저 이름을 다른 이름으로 업데이트 하면 변경된 이름을 반환합니다.")
  @Test
  void updateUserName() {
    UserResult user = userService.updateName(setUpUser.id(), OTHER_USER.getName());

    assertThat(user.name()).isEqualTo(OTHER_USER.getName());
  }

  @DisplayName("유저 프로필 이미지를 업데이트하면 변경된 이미지를 반환합니다.")
  @Test
  void updateProfileImage() {
    UserCreateRequest mockUserRequest = createMockUserRequest(OTHER_USER.getName(),
        OTHER_USER.getEmail());
    UserResult user = userService.register(mockUserRequest, createMockImageFile("dog.jpg"));

    MultipartFile otherImageFile = createMockImageFile("Kirby.jpg");
    UserResult updateProfileUser = userService.updateProfileImage(user.id(), otherImageFile);
    BinaryContent binaryContent = binaryContentRepository.findById(updateProfileUser.profileId())
        .get();

    assertAll(
        () -> assertThat(otherImageFile.getBytes()).isEqualTo(binaryContent.getBytes()),
        () -> assertThat(binaryContentRepository.findById(user.profileId())).isEmpty()
    );
  }


  @DisplayName("유저가 삭제된다면 연관된 userStatus랑 binaryContent도 삭제됩니다.")
  @Test
  void deleteWithUserStatus() {
    userService.delete(setUpUser.id());

    assertAll(
        () -> assertThat(userRepository.findById(setUpUser.id())).isEmpty(),
        () -> assertThat(userStatusRepository.findByUserId(setUpUser.id())).isEmpty(),
        () -> assertThat(binaryContentRepository.findById(setUpUser.profileId())).isEmpty()
    );
  }

  @DisplayName("유저를 삭제 후에 조회 한다면 예외를 반환합니다.")
  @Test
  void deleteUserAndVerifyNotFound() {
    UUID setUpUserId = setUpUser.id();
    userService.delete(setUpUserId);

    assertThatThrownBy(() -> userService.getById(setUpUserId))
        .isInstanceOf(IllegalArgumentException.class);
  }
}