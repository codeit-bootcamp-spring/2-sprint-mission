package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @Mock private BinaryContentRepository binaryContentRepository;
  @Mock private BinaryContentStorage binaryContentStorage;
  @Mock private UserStatusRepository userStatusRepository;

  @InjectMocks
  private BasicUserService userService;

  @Test
  @DisplayName("사용자 생성 성공")
  void createUser_success() {
    // given
    UserCreateRequest request = new UserCreateRequest("tester", "test@example.com", "pass1234");
    User user = new User("tester", "test@example.com", "pass1234", null);
    UserDto userDto = new UserDto(UUID.randomUUID(), "tester", "test@example.com", null,true);

    given(userRepository.existsByEmail("test@example.com")).willReturn(false);
    given(userRepository.existsByUsername("tester")).willReturn(false);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    // when
    UserDto result = userService.create(request, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo("tester");
    assertThat(result.email()).isEqualTo("test@example.com");
  }

  @Test
  @DisplayName("이미 존재하는 이메일일 경우 예외 발생")
  void createUser_emailDuplicate_fail() {
    // given
    UserCreateRequest request = new UserCreateRequest("tester", "duplicate@example.com", "pass1234");

    given(userRepository.existsByEmail("duplicate@example.com")).willReturn(true);

    // when & then
    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessageContaining(ErrorCode.DUPLICATE_USER.getMessage());
  }

  @Test
  @DisplayName("사용자 정보 수정 성공")
  void updateUser_success() {
    UUID userId = UUID.randomUUID();
    User user = new User("oldname", "old@email.com", "oldpass", null);
    UserUpdateRequest request = new UserUpdateRequest("newname", "new@email.com", "newpass");
    UserDto updatedDto = new UserDto(userId, "newname", "new@email.com", null,true);

    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByEmail("new@email.com")).willReturn(false);
    given(userRepository.existsByUsername("newname")).willReturn(false);
    given(userMapper.toDto(user)).willReturn(updatedDto);

    UserDto result = userService.update(userId, request, Optional.empty());

    assertThat(result.username()).isEqualTo("newname");
    assertThat(result.email()).isEqualTo("new@email.com");
  }

  @Test
  @DisplayName("수정 시 존재하지 않는 사용자이면 예외 발생")
  void updateUser_userNotFound_fail() {
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newname", "new@email.com", "newpass");

    given(userRepository.findById(userId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> userService.update(userId, request, Optional.empty()))
      .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("User with id " + userId + " not found");
  }

  @Test
  @DisplayName("사용자 삭제 성공")
  void deleteUser_success() {
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(true);

    userService.delete(userId);

    verify(userRepository).deleteById(userId);
  }

  @Test
  @DisplayName("삭제 시 존재하지 않는 사용자이면 예외 발생")
  void deleteUser_userNotFound_fail() {
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(false);

    assertThatThrownBy(() -> userService.delete(userId))
    .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("User with id " + userId + " not found");
  }
}
