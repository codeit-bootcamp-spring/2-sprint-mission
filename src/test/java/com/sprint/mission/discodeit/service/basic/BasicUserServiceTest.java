package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.exception.common.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService basicUserService;

  private UUID userId;
  private String userName;
  private String email;
  private String password;
  private User existingUser;
  private UserDto expectedUserDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    userName = "testUser";
    email = "test@test";
    password = "password";

    existingUser = new User(userName, email, password, null);
    expectedUserDto = new UserDto(userId, userName, email, null, true);
  }

  @Test
  @DisplayName("사용자 생성 성공")
  void testCreateUserSuccess() {
    UserCreateRequest userCreateRequest = new UserCreateRequest(userName, email, password);

    given(userRepository.existsByEmail(email)).willReturn(false);
    given(userRepository.existsByUsername(userName)).willReturn(false);

    User savedUser = new User(userName, email, password, null);
    given(userRepository.save(any(User.class))).willReturn(savedUser);
    given(userMapper.toDto(any(User.class))).willReturn(expectedUserDto);

    UserDto result = basicUserService.create(userCreateRequest, Optional.empty());

    assertEquals(expectedUserDto, result);

    then(userRepository).should().existsByEmail(email);
    then(userRepository).should().existsByUsername(userName);
    then(userRepository).should().save(any(User.class));
    then(userMapper).should().toDto(any(User.class));
  }

  @Test
  @DisplayName("사용자 생성 실패 - 이메일 중복")
  void testCreateUserFailure_emailDuplicate() {
    UserCreateRequest userCreateRequest = new UserCreateRequest(userName, email, password);

    given(userRepository.existsByEmail(email)).willReturn(true);

    UserException exception = assertThrows(UserException.class, () -> {
      basicUserService.create(userCreateRequest, Optional.empty());
    });

    assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage(), exception.getMessage());
    assertTrue(exception.getDetails().containsKey("email"));
    assertEquals(email, exception.getDetails().get("email"));

    then(userRepository).should().existsByEmail(email);
    then(userRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("사용자 업데이트 성공")
  void testUpdateUserSuccess() {
    String newUsername = "testUser2";
    String newEmail = "test2@test";
    String newPassword = "password";
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newUsername, newEmail, newPassword);

    User updatedUser = new User(newUsername, newEmail, newPassword, null);
    UserDto updatedUserDto = new UserDto(userId, newUsername, newEmail, null, true);

    given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
    given(userRepository.existsByEmail(newEmail)).willReturn(false);
    given(userRepository.existsByUsername(newUsername)).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(updatedUser);
    given(userMapper.toDto(any(User.class))).willReturn(updatedUserDto);

    UserDto result = basicUserService.update(userId, userUpdateRequest, Optional.empty());

    assertEquals(updatedUserDto, result);

    then(userRepository).should().findById(userId);
    then(userRepository).should().existsByEmail(newEmail);
    then(userRepository).should().existsByUsername(newUsername);
    then(userRepository).should().save(any(User.class));
    then(userMapper).should().toDto(any(User.class));
  }

  @Test
  @DisplayName("사용자 업데이트 실패 - 사용자 이름 중복")
  void testUpdateUserFailure_usernameDuplicate() {
    String newUsername = "testUser";
    String newEmail = "test@test";
    String newPassword = "password";
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newUsername, newEmail, newPassword);

    given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
    given(userRepository.existsByEmail(newEmail)).willReturn(false);
    given(userRepository.existsByUsername(newUsername)).willReturn(true);

    UserException exception = assertThrows(UserException.class, () -> {
      basicUserService.update(userId, userUpdateRequest, Optional.empty());
    });

    assertEquals(ErrorCode.USERNAME_ALREADY_EXISTS.getMessage(), exception.getMessage());
    assertTrue(exception.getDetails().containsKey("newUsername"));
    assertEquals(newUsername, exception.getDetails().get("newUsername"));

    then(userRepository).should().existsByEmail(newEmail);
    then(userRepository).should().existsByUsername(newUsername);
    then(userRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  @DisplayName("사용자 삭제 성공")
  void testDeleteUserSuccess() {
    given(userRepository.existsById(userId)).willReturn(true);

    basicUserService.delete(userId);

    then(userRepository).should().deleteById(userId);
  }

  @Test
  @DisplayName("사용자 삭제 실패 - 사용자 존재하지 않음")
  void testDeleteUserFailure_userNotFound() {
    given(userRepository.existsById(userId)).willReturn(false);

    UserException exception = assertThrows(UserException.class, () -> {
      basicUserService.delete(userId);
    });

    assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), exception.getMessage());
    assertTrue(exception.getDetails().containsKey("userId"));
    assertEquals(userId.toString(), exception.getDetails().get("userId").toString());

    then(userRepository).should().existsById(userId);
    then(userRepository).shouldHaveNoMoreInteractions();
  }
}
