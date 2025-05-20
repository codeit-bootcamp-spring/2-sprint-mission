package com.sprint.mission.discodeit.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserStatusRepository userStatusRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private BinaryContentService binaryContentService;

  @Mock
  private UserStatusService userStatusService;

  @InjectMocks
  private BasicUserService userService;


  @DisplayName("유저 생성 테스트 with profile - 성공")
  @Test
  void createUser_WithProfile_Success() {
    // given
    CreateUserRequest request = new CreateUserRequest("test@example.com", "test", "test");

    User user = User.builder()
        .username(request.username())
        .email(request.email())
        .password("hashedPassword")
        .build();

    MockMultipartFile profile = mock(MockMultipartFile.class);
    BinaryContent binaryContent = mock(BinaryContent.class);
    UserStatus userStatus = mock(UserStatus.class);

    UserDto userDto = new UserDto(
        UUID.randomUUID(),
        "test",
        "test@example.com",
        null,
        false);

    given(userRepository.existsByUsername("test")).willReturn(false);
    given(userRepository.existsByEmail("test@example.com")).willReturn(false);
    given(userRepository.save(argThat(savedUser ->
        savedUser.getUsername().equals("test") &&
            savedUser.getEmail().equals("test@example.com")
    ))).willReturn(user);
    given(binaryContentService.createBinaryContent(profile)).willReturn(
        binaryContent);
    given(userStatusService.create(any(User.class))).willReturn(userStatus);
    given(userMapper.toDto(argThat(savedUser ->
        savedUser.getUsername().equals("test") &&
            savedUser.getEmail().equals("test@example.com")
    ))).willReturn(userDto);

    // when
    UserDto result = userService.createUser(request, profile);

    // then
    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo("test");
    assertThat(result.email()).isEqualTo("test@example.com");
    then(binaryContentService).should(times(1)).createBinaryContent(any());
  }

  @DisplayName("유저 생성 테스트 without profile - 성공")
  @Test
  void createUser_WithoutProfile_Success() {
    // given
    CreateUserRequest request = new CreateUserRequest("test@example.com", "test", "test");

    User user = User.builder()
        .username(request.username())
        .email(request.email())
        .password("hashedPassword")
        .build();

    UserStatus userStatus = mock(UserStatus.class);

    UserDto userDto = new UserDto(
        UUID.randomUUID(),
        "test",
        "test@example.com",
        null,
        false);

    given(userRepository.existsByUsername("test")).willReturn(false);
    given(userRepository.existsByEmail("test@example.com")).willReturn(false);
    given(userRepository.save(argThat(savedUser ->
        savedUser.getUsername().equals("test") &&
            savedUser.getEmail().equals("test@example.com")
    ))).willReturn(user);
    given(userStatusService.create(any(User.class))).willReturn(userStatus);
    given(userMapper.toDto(argThat(savedUser ->
        savedUser.getUsername().equals("test") &&
            savedUser.getEmail().equals("test@example.com")
    ))).willReturn(userDto);

    // when
    UserDto result = userService.createUser(request, null);

    // then
    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo("test");
    assertThat(result.email()).isEqualTo("test@example.com");
  }


  @DisplayName("유저 생성 테스트 - 이메일 중복 체크")
  @Test
  void createUserEmailDuplicate() {
    // given
    CreateUserRequest request = new CreateUserRequest("test@example.com", "test", "test");

    given(userRepository.existsByEmail("test@example.com")).willReturn(true);

    // when
    // then
    assertThatThrownBy(() -> userService.createUser(request, null))
        .isInstanceOf(DiscodeitException.class)
        .hasMessageContaining(ErrorCode.EMAIL_ALREADY_EXISTS.name());
  }

  @DisplayName("유저 생성 테스트 - 유저명 중복 체크")
  @Test
  void createUserUsernameDuplicate() {
    // given
    CreateUserRequest request = new CreateUserRequest("test@example.com", "test", "test");

    given(userRepository.existsByUsername("test")).willReturn(true);

    // when
    // then
    assertThatThrownBy(() -> userService.createUser(request, null))
        .isInstanceOf(DiscodeitException.class)
        .hasMessageContaining(ErrorCode.USERNAME_ALREADY_EXISTS.name());
  }

  @DisplayName("유저 업데이트 테스트 - 성공")
  @Test
  void updateUserSuccess() {
    // given
    UUID userId = UUID.randomUUID();
    UpdateUserRequest request = new UpdateUserRequest("newJohn", "new@example.com", "newPass");

    User user = mock(User.class);

    UserDto updatedUserDto = new UserDto(userId, "newJohn", "new@example.com", null, false);

    given(userMapper.toDto(user)).willReturn(updatedUserDto);
    given(userRepository.findWithDetailsById(userId)).willReturn(Optional.of(user));

    // when
    UserDto result = userService.updateUser(userId, request, null);

    // then
    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo("newJohn");
  }

  @DisplayName("유저 업데이트 테스트 - 존재하지 않는 유저 실패")
  @Test
  void updateUserNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    UpdateUserRequest request = new UpdateUserRequest("newJohn", "new@example.com", "newPass");

    given(userRepository.findWithDetailsById(userId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> userService.updateUser(userId, request, null))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.name());
  }

  @DisplayName("유저 삭제 테스트 - 성공")
  @Test
  void deleteUserSuccess() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(true);

    // when
    userService.deleteUser(userId);

    // then
    then(userRepository).should().deleteById(userId);
  }

  @DisplayName("유저 삭제 테스트 - 존재하지 않는 유저")
  @Test
  void deleteUserNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(false);

    // when
    // then
    assertThatThrownBy(() -> userService.deleteUser(userId))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.name());
  }
}
