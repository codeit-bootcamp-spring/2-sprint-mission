package service;

import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.user.CreateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.CreateUserResult;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserMapperImpl;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapperImpl;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {

  // 공유 의존성 -> 테스트 대역(Mock)으로 교체
  @Mock
  UserRepository userRepository;
  @Mock
  UserStatusService userStatusService;
  @Mock
  BinaryContentService binaryContentService;
  @Mock
  BinaryContentStorage binaryContentStorage;

  // DB 접근(프로세스 외부 의존성?)도 아닌, 단순 Mapper이므로 Spy 객체 사용
  // -> 공유 의존성이 없으므로 Spy 객체나 실제 객체를 사용해도 단위 테스트의 조건을 깨뜨리지 않는다. (고전파)
  @Spy
  UserMapper userMapper = new UserMapperImpl();
  @Spy
  UserStatusMapper userStatusMapper = new UserStatusMapperImpl();

  @InjectMocks
  BasicUserService basicUserService;

  // 고전파 - 공유 의존성이 없는 비공개 의존성의 경우 실제 객체 사용
  private User user;
  private UserStatus userStatus;
  private BinaryContent profile;
  private MultipartFile mockFile;

  // 테스트 메서드 실행 전마다 새로운 객체 생성 -> 공유 의존성 방지
  @BeforeEach
  void setUp() {
    profile = BinaryContent.builder()
        .contentType("image/png")
        .filename("test.png")
        .size(9)
        .build();

    user = User.builder()
        .username("test")
        .password("1234")
        .email("test@test.com")
        .profile(profile)
        .build();
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());

    userStatus = new UserStatus(user, Instant.now());

    user.updateUserStatus(userStatus);

    mockFile = new MockMultipartFile(
        "testProfile",
        "test.png",
        "image/png",
        "test data".getBytes());
  }


  @Test
  @DisplayName("유저 생성 성공")
  void createUser_success() {
    // given (BDDMockito 사용)
    CreateUserCommand createUserCommand = new CreateUserCommand("test", "test@test.com", "1234");
    CreateBinaryContentResult createBinaryContentResult = new CreateBinaryContentResult(
        UUID.randomUUID(), "test.png", 9,
        "image/png");

    given(userRepository.existsByUsername(createUserCommand.username())).willReturn(false);
    given(userRepository.existsByEmail(createUserCommand.email())).willReturn(false);
    given(binaryContentService.create(any())).willReturn(createBinaryContentResult);

    // when
    CreateUserResult createUserResult = basicUserService.create(createUserCommand, mockFile);

    // then
    assertThat(createUserCommand.username()).isEqualTo(createUserResult.username());
    assertThat(createUserCommand.email()).isEqualTo(createUserResult.email());
    // 어떤 mockFile이 들어오든, binaryContentService나 binaryContentStorage는 createBinaryContentResult를 반환하는데,
    // 과연 mockFile과 createUserResult.profile을 비교하는 것이 의미가 있을까?
    // userService 단위테스트에서는 binaryContentService, Storage와는 상관없이 작동해야하기 때문에, mockFile이 createUserResult.profile에 잘 저장되는지 정도만 확인하면 될 것 같음
    assertThat(mockFile.getOriginalFilename()).isEqualTo(createUserResult.profile().filename());
    assertThat(mockFile.getSize()).isEqualTo(createUserResult.profile().size());
    assertThat(createUserResult.online()).isTrue();

    then(userRepository).should(times(1)).save(any(User.class));
    then(binaryContentService).should(times(1)).create(any());
    then(binaryContentStorage).should(times(1)).put(any(), any());
  }

  @Test
  @DisplayName("유저 생성할 때, username 중복으로 인한 실패")
  void createUser_duplicateUsername_failed() {
    // given
    CreateUserCommand createUserCommand = new CreateUserCommand("test", "test@test.com", "1234");
    given(userRepository.existsByUsername(createUserCommand.username())).willReturn(true);

    // when + then
    assertThatThrownBy(() -> basicUserService.create(createUserCommand, mockFile))
        .isInstanceOf(DuplicateUsernameException.class)
        .hasMessageContaining("Username exists already");

    then(userRepository).should(times(1)).existsByUsername(createUserCommand.username());
    then(userRepository).should(never()).save(any(User.class));
  }

  @Test
  @DisplayName("유저 생성할 때, email 중복으로 인한 실패")
  void createUser_duplicateEmail_failed() {
    // given
    CreateUserCommand createUserCommand = new CreateUserCommand("test", "test@test.com", "1234");
    given(userRepository.existsByUsername(createUserCommand.username())).willReturn(false);
    given(userRepository.existsByEmail(createUserCommand.email())).willReturn(true);

    // when + then
    assertThatThrownBy(() -> basicUserService.create(createUserCommand, mockFile))
        .isInstanceOf(DuplicateEmailException.class)
        .hasMessageContaining("Email exists already");

    then(userRepository).should(times(1)).existsByEmail(createUserCommand.email());
    then(userRepository).should((never())).save(any(User.class));
  }

  @Test
  @DisplayName("유저 수정 성공")
  void updateUser_success() {
    // given
    UpdateUserCommand updateUserCommand = new UpdateUserCommand("updateTest", "updateTest@test.com",
        "update1234");
    CreateBinaryContentResult createBinaryContentResult = new CreateBinaryContentResult(
        UUID.randomUUID(), "test.png", 9,
        "image/png");

    given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));
    given(binaryContentService.create(any())).willReturn(createBinaryContentResult);

    // when
    UpdateUserResult updateUserResult = basicUserService.update(user.getId(),
        updateUserCommand,
        mockFile);

    // then
    assertThat(updateUserCommand.newEmail()).isEqualTo(updateUserResult.email());
    assertThat(updateUserCommand.newUsername()).isEqualTo(updateUserResult.username());
    assertThat(mockFile.getOriginalFilename()).isEqualTo(updateUserResult.profile().filename());
    assertThat(mockFile.getSize()).isEqualTo(updateUserResult.profile().size());

    then(userRepository).should(times(1)).findById(user.getId());
    then(userRepository).should(times(1)).save(any(User.class));
    then(binaryContentService).should(times(1)).create(any());
    then(binaryContentStorage).should(times(1)).put(any(), any());
  }

  @Test
  @DisplayName("유저 수정할 때, 해당 유저를 찾지 못해 실패")
  void updateUser_userNotFound_failed() {
    // given
    UUID userId = user.getId();
    given(userRepository.findById(userId)).willReturn(Optional.empty());
    UpdateUserCommand updateUserCommand = new UpdateUserCommand("updateTest", "updateTest@test.com",
        "update1234");

    // when + then
    assertThatThrownBy(
        () -> basicUserService.update(userId, updateUserCommand, mockFile))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining("User not found");

    then(userRepository).should(times(1)).findById(userId);
    then(userRepository).should(never()).save(any(User.class));
  }

  @Test
  @DisplayName("유저 삭제 성공")
  void deleteUser_success() {
    // given
    given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));

    // when
    basicUserService.delete(user.getId());

    // then
    then(userRepository).should(times(1)).deleteById(user.getId());
    then(binaryContentService).should(times(1)).delete(user.getProfile().getId());
    then(binaryContentStorage).should(times(1)).delete(user.getProfile().getId());
    then(userStatusService).should(times(1)).deleteByUserId(user.getId());
  }

  @Test
  @DisplayName("유저 삭제할 때, 해당 유저를 찾지 못해 실패")
  void deleteUser_userNotFound_failed() {
    // given
    UUID userId = user.getId();
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when + then
    assertThatThrownBy(() -> basicUserService.delete(userId))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining("User not found");

    then(userRepository).should(never()).deleteById(userId);
    then(binaryContentService).should(never()).delete(any(UUID.class));
    then(binaryContentStorage).should(never()).delete(any(UUID.class));
    then(userStatusService).should(never()).deleteByUserId(userId);
  }
}



