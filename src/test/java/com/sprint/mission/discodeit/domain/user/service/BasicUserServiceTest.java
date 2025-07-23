package com.sprint.mission.discodeit.domain.user.service;

import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

class BasicUserServiceTest extends IntegrationTestSupport {

  private static final String USER_NAME = "hwang";
  private static final String USER_EMAIL = "h@naver.com";
  private static final String USER_PASSWORD = "1234";

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Autowired
  private UserService userService;

//  @MockitoBean
//  private S3Adapter s3Adapter; // 로컬 스택으로 개선 예정 // 어댑터를 모킹하면 오히려 작동을 안하는데
  @MockitoBean
  private S3Client s3Client;

  @BeforeEach
  void beforeEach() {
    userRepository.deleteAllInBatch();
    binaryContentRepository.deleteAllInBatch();
  }

  @DisplayName("유저 등록을 요청하면, 유저와 유저의 상태를 저장한다")
  @Test
  void register() {
    // given
    UserCreateRequest userRequest = new UserCreateRequest(USER_NAME, USER_EMAIL, USER_PASSWORD);

    // when
    UserResult user = userService.register(userRequest, null);

    // then
    Assertions.assertThat(user)
        .extracting(UserResult::id)
        .isEqualTo(user.id());
  }

  @Disabled("s3Adapter를 모킹하면 SUCCESS가 아니라 Waiting이 나옵니다. 비동기 처리시 모킹 주의")
  @DisplayName("프로필 사진과 함께 유저 등록을 요청하면, 프로필 사진을 저장합니다.")
  @Test
  void register_WithProfileImage() {
    // given
    UserCreateRequest userRequest = new UserCreateRequest(USER_NAME, USER_EMAIL, USER_PASSWORD);
    String fileName = UUID.randomUUID().toString();
    BinaryContentRequest binaryContentRequest = new BinaryContentRequest(fileName, "", 0,
        "hello".getBytes());
    PutObjectResponse mockResponse = PutObjectResponse.builder()
        .eTag("mock-etag")
        .build();
//    BDDMockito.given(s3Adapter.put(any(PutObjectRequest.class), any(RequestBody.class)))
//        .willReturn(CompletableFuture.completedFuture(mockResponse));
    BDDMockito.given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .willReturn(mockResponse);

    // when
    UserResult user = userService.register(userRequest, binaryContentRequest);

    // then
    Assertions.assertThat(user)
        .extracting(UserResult::id)
        .isEqualTo(user.id());

    Awaitility.await()
        .atMost(Duration.ofSeconds(12))
        .pollInterval(Duration.ofMillis(1000))
        .untilAsserted(() -> {
          Assertions.assertThat(binaryContentRepository.findAll())
              .hasSize(1)
              .extracting(BinaryContent::getFileName, BinaryContent::getBinaryContentUploadStatus)
              .containsExactlyInAnyOrder(Tuple.tuple(fileName, BinaryContentUploadStatus.SUCCESS));
        });
  }


  @DisplayName("이미 등록된 유저 이름으로 등록 시도시, 예외를 반환한다")
  @Test
  void register_DuplicateNameException() {
    // given
    userRepository.save(
        new User(USER_NAME, UUID.randomUUID().toString(), UUID.randomUUID().toString(), null));
    UserCreateRequest userRequest = new UserCreateRequest(USER_NAME, UUID.randomUUID().toString(),
        UUID.randomUUID().toString());

    // when & then
    Assertions.assertThatThrownBy(() -> userService.register(userRequest, null))
        .isInstanceOf(UserAlreadyExistsException.class);
  }

  @DisplayName("이미 등록된 유저 이메일로 등록 시도시, 예외를 반환한다")
  @Test
  void register_DuplicateEmailException() {
    // given
    userRepository.save(
        new User(UUID.randomUUID().toString(), USER_EMAIL, UUID.randomUUID().toString(), null));
    UserCreateRequest userRequest = new UserCreateRequest(UUID.randomUUID().toString(), USER_EMAIL,
        UUID.randomUUID().toString());

    // when & then
    Assertions.assertThatThrownBy(() -> userService.register(userRequest, null))
        .isInstanceOf(UserAlreadyExistsException.class);
  }

  @DisplayName("유저 ID로 조회하면, 해당 유저를 반환한다")
  @Test
  void getById() {
    // given
    User savedUser = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));

    // when
    UserResult user = userService.getById(savedUser.getId());

    // then
    Assertions.assertThat(user)
        .extracting(UserResult::id)
        .isEqualTo(savedUser.getId());
  }

  @DisplayName("유저 ID로 조회할때 유저가 없다면, 예외를 반환한다")
  @Test
  void getById_EntityNotFound() {
    // when & then
    Assertions.assertThatThrownBy(() -> userService.getById(UUID.randomUUID()))
        .isInstanceOf(UserNotFoundException.class);
  }

  @DisplayName("유저 이름으로 조회시, 해당 유저를 반환한다.")
  @Test
  void getByName() {
    // given
    userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));

    // when
    UserResult user = userService.getByName(USER_NAME);

    // then
    Assertions.assertThat(user.username()).isEqualTo(USER_NAME);
  }

  @DisplayName("유저 이름으로 조회시 해당 이름의 유저가 없다면, 예외를 반환한다.")
  @Test
  void getByName_Exception() {
    // when & then
    Assertions.assertThatThrownBy(() -> userService.getByName(USER_NAME))
        .isInstanceOf(UserNotFoundException.class);
  }

  @DisplayName("전체 유저를 반환한다.")
  @Test
  void getAllIn() {
    // given
    User saveUser = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));
    User saveAnotherUser = userRepository.save(
        new User(USER_NAME + 2, USER_EMAIL + 2, USER_PASSWORD, null));

    // when
    List<UserResult> users = userService.getAllIn();

    // then
    Assertions.assertThat(users)
        .extracting(UserResult::id)
        .containsExactlyInAnyOrder(saveUser.getId(), saveAnotherUser.getId());
  }

  @DisplayName("이메일로 조회 시, 유저를 성공적으로 반환한다")
  @Test
  void getByEmail() {
    // given
    User save = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));

    // when
    UserResult user = userService.getByEmail(USER_EMAIL);

    // then
    Assertions.assertThat(user.id()).isEqualTo(save.getId());
  }

  @DisplayName("이메일로 조회 시, 존재하지 않으면 예외를 반환한다")
  @Test
  void getByEmail_EntityNotFound() {
    Assertions.assertThatThrownBy(() -> userService.getByEmail("notfound@example.com"))
        .isInstanceOf(UserNotFoundException.class);
  }

  @DisplayName("업데이트하면, 유저 정보가 수정된다")
  @Test
  void updateUser() {
    // given
    User user = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));
    UserUpdateRequest request = new UserUpdateRequest(
        "newName", "newEmail@example.com", "newPassword");

    // when
    UserResult updatedUser = userService.update(user.getId(), request, null);

    // then
    Assertions.assertThat(updatedUser.username()).isEqualTo("newName");
    Assertions.assertThat(updatedUser.email()).isEqualTo("newEmail@example.com");
  }

  @DisplayName("등록되지 않은 유저 정보로 업데이트 시도시, 예외를 반환한다")
  @Test
  void updateUser_NoUserException() {
    // given
    UserUpdateRequest request = new UserUpdateRequest("newName", "newEmail@example.com",
        "newPassword");

    // when & then
    Assertions.assertThatThrownBy(() -> userService.update(UUID.randomUUID(), request, null))
        .isInstanceOf(UserNotFoundException.class);
  }

  @DisplayName("삭제하면, 유저와 유저 상태가 제거된다")
  @Test
  void deleteUser() {
    // given
    User user = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));

    // when
    userService.delete(user.getId());

    // then
    Assertions.assertThat(userRepository.existsById(user.getId())).isFalse();
  }

  @DisplayName("삭제하려는 유저가 없으면 예외를 던진다")
  @Test
  void deleteUser_NotFound() {
    // when & then
    Assertions.assertThatThrownBy(() -> userService.delete(UUID.randomUUID()))
        .isInstanceOf(UserNotFoundException.class);
  }

}