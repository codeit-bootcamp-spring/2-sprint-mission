package com.sprint.mission.discodeit.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.within;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.service.BinaryContentService;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.service.BasicUserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

  @Mock
  private JpaUserRepository userRepository;


  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private BinaryContentService binaryContentService;

  @InjectMocks
  private BasicUserService userService;

  private User user;
  private UUID userId;
  private BinaryContent oldProfile;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    oldProfile = BinaryContent.create("old.png", 0L, "image/png");
    user = User.create("a", "a@email.com", "a", oldProfile);
  }

  @Test
  @Disabled
  void UserCreate_WithNoProfile_Success() {
    // given
    UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "test");
    // when
    UserDto result = userService.create(request, Optional.empty());
    // then
    assertThat(result.username()).isEqualTo("test");
    assertThat(result.email()).isEqualTo("test@test.com");
    assertNull(result.profile());
    assertTrue(result.online());
  }

  @Test
  void UserCreate_WithProfile_Success() {
    // given
    UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "test");
    BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
        "a.png", "image/png", new byte[0]);
    when(binaryContentService.create(binaryContentCreateRequest)).thenReturn(
        BinaryContent.create("a.png", 0L, "image/png"));
    // when
    UserDto userDto = userService.create(request, Optional.of(binaryContentCreateRequest));
    // then
    assertThat(userDto.username()).isEqualTo("test");
    assertThat(userDto.email()).isEqualTo("test@test.com");
    assertNotNull(userDto.profile());
    assertTrue(userDto.online());
  }

  @Test
  void CreateUser_WithSameName_ShouldThrowException() {
    // given
    UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "test");
    // when
    when(userRepository.existsByNameOrEmail(request.username(), request.email())).thenReturn(true);
    // then
    assertThrows(UserException.class, () -> {
      userService.create(request, Optional.empty());
    });
  }

  @Test
  void CreateUser_WithSameEmail_ShouldThrowException() {
    // given
    UserCreateRequest request = new UserCreateRequest("b", "a@a.com", "test");
    // when
    when(userRepository.existsByNameOrEmail(request.username(), request.email())).thenReturn(true);
    // then
    assertThrows(UserException.class, () -> {
      userService.create(request, Optional.empty());
    });
  }

  @Test
  @Disabled
  void CreateUser_WithoutName_ShouldThrowException() {
    // given
    UserCreateRequest request = new UserCreateRequest(null, "test@test.com", "test");
    // when & then
    assertThrows(UserException.class, () -> {
      userService.create(request, Optional.empty());
    });
  }

  @Test
  @Disabled
  void CreateUser_WithoutEmail_ShouldThrowException() {
    // given
    UserCreateRequest request = new UserCreateRequest("test", null, "test");
    // when & then
    assertThrows(UserException.class, () -> {
      userService.create(request, Optional.empty());
    });
  }

  @Test
  void UserUpdate_WithImage_Success() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newName", "newEmail", "newPassword");
    BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest("a.png",
        "image/png", new byte[0]);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(binaryContentService.create(binaryContentCreateRequest)).thenReturn(
        BinaryContent.create("a.png", 0L, "image/png"));
    when(userRepository.save(user)).thenReturn(user);

    // when
    UserDto result = userService.update(userId, request, Optional.of(binaryContentCreateRequest));

    // then
    assertNotNull(result.profile());

    assertThat(result.username()).isEqualTo("newName");
    assertThat(result.email()).isEqualTo("newEmail");
    assertThat(result.profile().fileName()).isEqualTo("a.png");
    assertThat(result.profile().fileName()).isNotEqualTo("old.png");
  }

  @Test
  void UserUpdate_WithoutImage_Success() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newName", "newEmail", "newPassword");

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    // when
    UserDto result = userService.update(userId, request, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo("newName");
    assertThat(result.email()).isEqualTo("newEmail");
    assertThat(result.profile().fileName()).isEqualTo("old.png");
  }

  @Test
  void UserUpdate_WithoutUser_ShouldThrowException() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newName", "newEmail", "newPassword");

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserException.class, () -> {
      userService.update(userId, request, Optional.empty());
    });
  }

  @Test
  void UserDeleteTest_Success() {
    // given
    User spyUser = spy(user);
    when(spyUser.getId()).thenReturn(userId);
    when(userRepository.findById(userId)).thenReturn(Optional.of(spyUser));
    // when
    userService.delete(userId);
    // then
    verify(userRepository).delete(spyUser);
  }

  @Test
  void UserDeleteTestFail() {
    // given
    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    // when
    assertThrows(UserException.class, () -> userService.delete(userId));
    // then
    verifyNoInteractions(binaryContentService);

    verify(userRepository, never()).deleteById(any());
  }

}
