package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

  @Test
  @DisplayName("Create User_성공")
  void createUser_shouldReturnCreatedUser() {
    // given
    UUID userId = UUID.randomUUID();
    UserCreateRequest request = new UserCreateRequest("bbori@dog.com", "bbori1234", "뽀리");

    User savedUser = User.create(request.username(), request.email(), request.password(), null);
    ReflectionTestUtils.setField(savedUser, "id", userId);

    UserDto userDto = new UserDto(userId, request.username(), request.email(), null, true);

    when(userRepository.save(any(User.class))).thenReturn(savedUser);
    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(userRepository.existsByUsername(request.username())).thenReturn(false);
    when(userMapper.toDto(savedUser, true)).thenReturn(userDto);

    // when
    UserDto result = userService.createUser(request, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo(request.username());
    assertThat(result.email()).isEqualTo(request.email());

    verify(userRepository).save(any(User.class));
    verify(userRepository).existsByEmail(request.email());
    verify(userRepository).existsByUsername(request.username());
    verify(userMapper).toDto(savedUser, true);
  }

  @Test
  @DisplayName("Create User_실패_중복 이메일")
  void createUser_shouldThrowException_whenEmailAlreadyExists() {
    // given
    UserCreateRequest request = new UserCreateRequest("bbori@dog.com", "bbori1234", "뽀리");
    when(userRepository.existsByEmail(request.email())).thenReturn(Boolean.TRUE);

    // when & then
    UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class,
        () -> userService.createUser(request, Optional.empty()));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_USER_EMAIL);
    assertThat(exception.getDetails().get("email")).isEqualTo(request.email());

    verify(userRepository).existsByEmail(request.email());
    verify(userRepository, never()).existsByUsername(any());
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("Create User_실패_중복 사용자 이름")
  void createUser_shouldThrowException_whenUsernameAlreadyExists() {
    // given
    UserCreateRequest request = new UserCreateRequest("bbori@dog.com", "bbori1234", "뽀리");
    when(userRepository.existsByEmail(request.email())).thenReturn(Boolean.FALSE);
    when(userRepository.existsByUsername(request.username())).thenReturn(Boolean.TRUE);

    // when & then
    UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class,
        () -> userService.createUser(request, Optional.empty()));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_USER_USERNAME);
    assertThat(exception.getDetails().get("username")).isEqualTo(request.username());

    verify(userRepository).existsByEmail(request.email());
    verify(userRepository).existsByUsername(request.username());
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("Update User_성공")
  void updateUser_shouldReturnUpdatedUser() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("new뽀리", "bbori@dog.net", "bbori4321@");

    User user = User.create("뽀리", "bbori@dog.com", "bbori1234!", null);
    ReflectionTestUtils.setField(user, "id", userId);

    UserStatus status = UserStatus.create(user, Instant.now());
    ReflectionTestUtils.setField(user, "status", status);

    UserDto userDto = new UserDto(userId, request.newUsername(), request.newEmail(), null, true);

    // Mock 설정
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.existsByEmail(request.newEmail())).thenReturn(false);
    when(userRepository.existsByUsername(request.newUsername())).thenReturn(false);
    when(userMapper.toDto(user, true)).thenReturn(userDto);

    // when
    UserDto result = userService.updateUser(userId, request, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo(request.newUsername());
    assertThat(result.email()).isEqualTo(request.newEmail());

    verify(userRepository).findById(userId);
    verify(userRepository).existsByEmail(request.newEmail());
    verify(userRepository).existsByUsername(request.newUsername());
    verify(userMapper).toDto(user, true);
  }

  @Test
  @DisplayName("Update User_실패_존재하지 않는 사용자")
  void updateUser_shouldThrowException_whenUserNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("new뽀리", "bbori@dog.net", "bbori4321@");

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // when & then
    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> userService.updateUser(userId, request, Optional.empty()));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    assertThat(exception.getDetails().get("userId")).isEqualTo(userId);

    verify(userRepository).findById(userId);
    verify(userRepository, never()).existsByEmail(any());
    verify(userRepository, never()).existsByUsername(any());
  }

  @Test
  @DisplayName("Delete User_성공")
  void deleteUser() {
    // given
    UUID userId = UUID.randomUUID();
    UUID profileId = UUID.randomUUID();

    BinaryContent profile = BinaryContent.create("bbori.png", 100L, "image/png");
    ReflectionTestUtils.setField(profile, "id", profileId);

    User user = User.create("뽀리", "bbori@dog.com", "bbori1234!", profile);
    ReflectionTestUtils.setField(user, "id", userId);

    UserStatus status = UserStatus.create(user, Instant.now());
    ReflectionTestUtils.setField(user, "status", status);

    // Mock 설정
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userStatusRepository.findByUserId(userId)).thenReturn(Optional.of(status));

    // when
    userService.deleteUser(userId);

    // then
    verify(userRepository).findById(userId);
    verify(userStatusRepository).findByUserId(userId);
    verify(binaryContentStorage).deleteById(profileId);
    verify(binaryContentRepository).deleteById(profileId);
    verify(userStatusRepository).deleteById(status.getId());
    verify(userRepository).deleteById(userId);
  }

  @Test
  @DisplayName("Delete User_실패_사용자가 존재하지 않을 때")
  void deleteUser_shouldThrowException_whenUserNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    UUID profileId = UUID.randomUUID();

    BinaryContent profile = BinaryContent.create("bbori.png", 100L, "image/png");
    ReflectionTestUtils.setField(profile, "id", profileId);

    User user = User.create("뽀리", "bbori@dog.com", "bbori1234!", profile);
    ReflectionTestUtils.setField(user, "id", userId);

    // Mock 설정
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // when & then
    UserNotFoundException exception = assertThrows(UserNotFoundException.class,
        () -> userService.deleteUser(userId));

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    assertThat(exception.getDetails().get("userId")).isEqualTo(userId);

    verify(userRepository).findById(userId);
    verify(userRepository, never()).deleteById(userId);
    verifyNoMoreInteractions(userStatusRepository, binaryContentStorage, binaryContentRepository);
  }
}
