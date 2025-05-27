package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks
  private BasicUserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserStatusRepository userStatusRepository;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private UserMapper userMapper;

  private UUID userId;
  private String username;
  private String email;
  private String password;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    username = "testUser";
    email = "test@example.com";
    password = "password123";

    user = User.create(username, email, password, null);
    ReflectionTestUtils.setField(user, "id", userId);
    userDto = new UserDto(userId, username, email, null, true);
  }

  @Test
  @DisplayName("Create User_성공")
  void createUser_shouldReturnCreatedUser() {
    // given
    UserCreateRequest request = new UserCreateRequest("bbori@dog.com", "bbori1234", "뽀리");

    given(userRepository.save(any(User.class))).willReturn(user);
    given(userRepository.existsByEmail(request.email())).willReturn(false);
    given(userRepository.existsByUsername(request.username())).willReturn(false);
    given(userMapper.toDto(user)).willReturn(userDto);

    // when
    UserDto result = userService.createUser(request, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo(request.username());
    assertThat(result.email()).isEqualTo(request.email());

    then(userRepository).should().save(any(User.class));
    then(userRepository).should().existsByEmail(request.email());
    then(userRepository).should().existsByUsername(request.username());
    then(userMapper).should().toDto(user);
  }

  @Test
  @DisplayName("Create User_실패_중복 이메일")
  void createUser_shouldThrowException_whenEmailAlreadyExists() {
    // given
    UserCreateRequest request = new UserCreateRequest("bbori@dog.com", "bbori1234", "뽀리");

    given(userRepository.existsByEmail(request.email())).willReturn(Boolean.TRUE);

    // when & then
    UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class,
        () -> userService.createUser(request, Optional.empty()));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_USER_EMAIL);
    assertThat(exception.getDetails().get("email")).isEqualTo(request.email());

    then(userRepository).should().existsByEmail(request.email());
    then(userRepository).should(never()).existsByUsername(any());
    then(userRepository).should(never()).save(any(User.class));
  }

  @Test
  @DisplayName("Create User_실패_중복 사용자 이름")
  void createUser_shouldThrowException_whenUsernameAlreadyExists() {
    // given
    UserCreateRequest request = new UserCreateRequest("bbori@dog.com", "bbori1234", "뽀리");

    given(userRepository.existsByEmail(request.email())).willReturn(Boolean.FALSE);
    given(userRepository.existsByUsername(request.username())).willReturn(Boolean.TRUE);

    // when & then
    UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class,
        () -> userService.createUser(request, Optional.empty()));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_USER_USERNAME);
    assertThat(exception.getDetails().get("username")).isEqualTo(request.username());

    then(userRepository).should().existsByEmail(request.email());
    then(userRepository).should().existsByUsername(request.username());
    then(userRepository).should(never()).save(any(User.class));
  }

  @Test
  @DisplayName("Update User_성공")
  void updateUser_shouldReturnUpdatedUser() {
    // given
    UserUpdateRequest request = new UserUpdateRequest("new뽀리", "bbori@dog.net", "bbori4321@");

    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByEmail(request.newEmail())).willReturn(false);
    given(userRepository.existsByUsername(request.newUsername())).willReturn(false);
    given(userMapper.toDto(user)).willReturn(userDto);

    // when
    UserDto result = userService.updateUser(userId, request, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo(request.newUsername());
    assertThat(result.email()).isEqualTo(request.newEmail());

    then(userRepository).should().findById(userId);
    then(userRepository).should().existsByEmail(request.newEmail());
    then(userRepository).should().existsByUsername(request.newUsername());
    then(userMapper).should().toDto(user);
  }

  @Test
  @DisplayName("Update User_실패_존재하지 않는 사용자")
  void updateUser_shouldThrowException_whenUserNotFound() {
    // given
    UserUpdateRequest request = new UserUpdateRequest("new뽀리", "bbori@dog.net", "bbori4321@");

    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when & then
    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> userService.updateUser(userId, request, Optional.empty()));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    assertThat(exception.getDetails().get("userId")).isEqualTo(userId);

    then(userRepository).should().findById(userId);
    then(userRepository).should(never()).existsByEmail(any());
    then(userRepository).should(never()).existsByUsername(any());
  }

  @Test
  @DisplayName("Delete User_성공")
  void deleteUser() {
    // given
    UUID profileId = UUID.randomUUID();

    BinaryContent profile = BinaryContent.create("bbori.png", 100L, "image/png");
    ReflectionTestUtils.setField(profile, "id", profileId);

    UserStatus status = UserStatus.create(user, Instant.now());
    ReflectionTestUtils.setField(user, "status", status);

    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userStatusRepository.findByUserId(userId)).willReturn(Optional.of(status));

    // when
    userService.deleteUser(userId);

    // then
    then(userRepository).should().findById(userId);
    then(userStatusRepository).should().findByUserId(userId);
    then(binaryContentStorage).should().deleteById(profileId);
    then(binaryContentRepository).should().deleteById(profileId);
    then(userStatusRepository).should().deleteById(status.getId());
    then(userRepository).should().deleteById(userId);
  }

  @Test
  @DisplayName("Delete User_실패_사용자가 존재하지 않을 때")
  void deleteUser_shouldThrowException_whenUserNotFound() {
    // given
    UUID profileId = UUID.randomUUID();

    BinaryContent profile = BinaryContent.create("bbori.png", 100L, "image/png");
    ReflectionTestUtils.setField(profile, "id", profileId);

    // Mock 설정
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when & then
    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> userService.deleteUser(userId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    assertThat(exception.getDetails().get("userId")).isEqualTo(userId);

    then(userRepository).should().findById(userId);
    then(userRepository).should(never()).deleteById(userId);
    then(userStatusRepository).shouldHaveNoMoreInteractions();
    then(binaryContentStorage).shouldHaveNoMoreInteractions();
    then(binaryContentRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("findById_성공")
  void findById_success() {
    // given
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userMapper.toDto(user)).willReturn(userDto);

    // when
    UserDto result = userService.findById(userId);

    // then
    assertThat(result).isEqualTo(userDto);
    then(userRepository).should().findById(userId);
    then(userMapper).should().toDto(user);
  }

  @Test
  @DisplayName("findById_실패_사용자가 존재하지 않을 때")
  void findById_shouldThrowException_whenUserNotFound() {
    // given
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when & then
    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> userService.deleteUser(userId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    assertThat(exception.getDetails().get("userId")).isEqualTo(userId);

    then(userRepository).should().findById(userId);
    then(userMapper).should(never()).toDto(user);
  }

}
