package com.sprint.mission.discodeit.controller;

import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.util.mock.file.MockFile.createAnonymousImageFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.application.dto.user.UserCreationRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResponse;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class UserControllerTest {

  private UserStatusRepository userStatusRepository;
  private UserRepository userRepository;
  private BinaryContentRepository binaryContentRepository;
  private UserService userService;
  private BinaryContentService binaryContentService;
  private UserController userController;

  @BeforeEach
  void setUp() {
    userStatusRepository = new JCFUserStatusRepository();
    userRepository = new JCFUserRepository();
    binaryContentRepository = new JCFBinaryContentRepository();
    userService = new BasicUserService(userRepository, userStatusRepository);
    binaryContentService = new BasicBinaryContentService(binaryContentRepository);
    userController = new UserController(userService, binaryContentService,
        new BasicUserStatusService(userStatusRepository, userRepository));
  }

  @DisplayName("프로필 사진 저장을 선택하지 않았을 때 프로필 아이디가 null을 반환한다.")
  @Test
  void registerException() {
    UserCreationRequest userRequest = new UserCreationRequest(LOGIN_USER.getName(),
        LOGIN_USER.getEmail(),
        LOGIN_USER.getPassword());
    UserResult user = userController.register(userRequest, null).getBody();
    assertThat(user.profileId()).isNull();
  }

  @DisplayName("프로필 사진을 저장하면 파일 저장 경로를 반환한다.")
  @Test
  void register() throws IOException {
    MultipartFile imageFile = createAnonymousImageFile("dog.jpg");

    UserCreationRequest userRequest = new UserCreationRequest(OTHER_USER.getName(),
        OTHER_USER.getEmail(),
        OTHER_USER.getPassword());

    UserResult user = userController.register(userRequest, imageFile).getBody();
    BinaryContentResult binaryContentResult = binaryContentService.getById(user.profileId());

    assertThat(imageFile.getBytes()).isEqualTo(binaryContentResult.bytes());
  }

  @DisplayName("처음 등록한 유저의 로그인 상태는 false를 반환한다.")
  @Test
  void registerValidateUserStatus() {
    UserCreationRequest userRequest = new UserCreationRequest(OTHER_USER.getName(),
        OTHER_USER.getEmail(), LOGIN_USER.getPassword());
    UserResult user = userController.register(userRequest, null).getBody();
    UserResponse userResponse = userController.getById(user.id()).getBody();

    assertThat(userResponse.isLogin()).isFalse();
  }

  @DisplayName("사용자 프로필 이미지를 업데이트하면 변경된 이미지 정보가 반영된다.")
  @Test
  void updateProfileImage() throws IOException {
    MultipartFile imageFile = createAnonymousImageFile("dog.jpg");
    UserCreationRequest userRequest = new UserCreationRequest(OTHER_USER.getName(),
        OTHER_USER.getEmail(),
        OTHER_USER.getPassword());

    UserResult user = userController.register(userRequest, imageFile).getBody();

    MultipartFile otherImageFile = createAnonymousImageFile("Kirby.jpg");
    UserResult profileImageUpdatedUser = userController.updateProfileImage(user.id(),
        otherImageFile).getBody();
    BinaryContentResult binaryContentResult = binaryContentService.getById(
        profileImageUpdatedUser.profileId());

    assertThat(otherImageFile.getBytes()).isEqualTo(binaryContentResult.bytes());
  }

  @DisplayName("유저 삭제 시 프로필 이미지와 UserStatus도 삭제된다.")
  @Test
  void delete() {
    MultipartFile imageFile = createAnonymousImageFile("dog.jpg");
    UserCreationRequest userRequest = new UserCreationRequest(OTHER_USER.getName(),
        OTHER_USER.getEmail(),
        OTHER_USER.getPassword());

    UserResult user = userController.register(userRequest, imageFile).getBody();
    userController.delete(user.id());

    assertAll(
        () -> assertThat(userRepository.findById(user.id())).isEmpty(),
        () -> assertThat(userStatusRepository.findByUserId(user.id())).isEmpty(),
        () -> assertThat(binaryContentRepository.findById(user.profileId())).isEmpty()
    );
  }
}