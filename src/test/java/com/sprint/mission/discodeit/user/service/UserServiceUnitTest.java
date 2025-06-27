package com.sprint.mission.discodeit.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.within;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.service.BinaryContentService;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.service.UserStatusService;
import com.sprint.mission.discodeit.core.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserStatusRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserInvalidRequestException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.service.BasicUserService;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
  private UserStatusService userStatusService;

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
    user.setUserStatus(UserStatus.create(user, Instant.now()));
  }

  @Test
  @Disabled
  void UserCreate_WithNoProfile_Success() {
    // given
    UserCreateCommand command = new UserCreateCommand("test", "test@test.com", "test");
    // when
    UserDto result = userService.create(command, Optional.empty());
    // then
    assertThat(result.username()).isEqualTo("test");
    assertThat(result.email()).isEqualTo("test@test.com");
    assertNull(result.profile());
    assertTrue(result.online());
  }

  @Test
  @Disabled
  void UserCreate_WithProfile_Success() {
//    // given
//    UserCreateCommand command = new UserCreateCommand("test", "test@test.com", "test");
//    BinaryContentCreateCommand binaryContentCreateCommand = new BinaryContentCreateCommand(
//        "a.png", "image/png", new byte[0]);
//    when(binaryContentService.create(binaryContentCreateCommand)).thenReturn(
//        BinaryContent.create("a.png", 0L, "image/png"));
//    // when
//    UserDto userDto = userService.create(command, Optional.of(binaryContentCreateCommand));
//    // then
//    assertThat(userDto.username()).isEqualTo("test");
//    assertThat(userDto.email()).isEqualTo("test@test.com");
//    assertNotNull(userDto.profile());
//    assertTrue(userDto.online());
  }

  @Test
  @Disabled
  void CreateUser_WithSameName_ShouldThrowException() {
//    // given
//    UserCreateCommand command = new UserCreateCommand("test", "test@test.com", "test");
//    // when
//    when(userRepository.existsByName(command.username())).thenReturn(true);
//    // then
//    assertThrows(UserAlreadyExistsException.class, () -> {
//      userService.create(command, Optional.empty());
//    });
  }

  @Test
  @Disabled
  void CreateUser_WithSameEmail_ShouldThrowException() {
//    // given
//    UserCreateCommand command = new UserCreateCommand("b", "a@a.com", "test");
//    // when
//    when(userRepository.existsByEmail(command.email())).thenReturn(true);
//    // then
//    assertThrows(UserAlreadyExistsException.class, () -> {
//      userService.create(command, Optional.empty());
//    });
  }

  @Test
  void CreateUser_WithoutName_ShouldThrowException() {
    // given
    UserCreateCommand noNameCommand = new UserCreateCommand(null, "test@test.com", "test");
    // when & then
    assertThrows(UserInvalidRequestException.class, () -> {
      userService.create(noNameCommand, Optional.empty());
    });
  }

  @Test
  void CreateUser_WithoutEmail_ShouldThrowException() {
    // given
    UserCreateCommand noEmailCommand = new UserCreateCommand("test", null, "test");
    // when & then
    assertThrows(UserInvalidRequestException.class, () -> {
      userService.create(noEmailCommand, Optional.empty());
    });
  }

  @Test
  void UserUpdate_WithImage_Success() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateCommand command = new UserUpdateCommand(userId, "newName", "newEmail", "newPassword");
    BinaryContentCreateRequest contentCommand = new BinaryContentCreateRequest("a.png",
        "image/png", new byte[0]);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(binaryContentService.create(contentCommand)).thenReturn(
        BinaryContent.create("a.png", 0L, "image/png"));
    when(userRepository.save(user)).thenReturn(user);

    // when
    UserDto result = userService.update(command, Optional.of(contentCommand));

    // then
    assertNotNull(result.profile());

    assertThat(result.username()).isEqualTo("newName");
    assertThat(result.email()).isEqualTo("newEmail");
    assertThat(result.profile().fileName()).isEqualTo("a.png");
    assertThat(result.profile().fileName()).isNotEqualTo("old.png");
    then(binaryContentService).should().delete(any());
  }

  @Test
  void UserUpdate_WithoutImage_Success() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateCommand command = new UserUpdateCommand(userId, "newName", "newEmail", "newPassword");

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    // when
    UserDto result = userService.update(command, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo("newName");
    assertThat(result.email()).isEqualTo("newEmail");
    assertThat(result.profile().fileName()).isEqualTo("old.png");
  }

  @Test
  void UserUpdate_WithoutUser_ShouldThrowException() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateCommand command = new UserUpdateCommand(userId, "newName", "newEmail", "newPassword");

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> {
      userService.update(command, Optional.empty());
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
    verify(userStatusService).delete(userId);
    verify(userRepository).deleteById(userId);
  }

  @Test
  void UserDeleteTestFail() {
    // given
    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    // when
    assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
    // then
    verifyNoInteractions(binaryContentService);
    verifyNoInteractions(userStatusService);

    verify(userRepository, never()).deleteById(any());
  }

  @Test
  void UserStatusOnline_Success() {
    // given
    Instant now = Instant.now();
    UserStatusRequest request = new UserStatusRequest(now);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    // when
    UserStatusDto online = userService.online(userId, request);
    // then
    assertThat(online.lastActiveAt()).isCloseTo(now, within(1, ChronoUnit.SECONDS));
  }

  @Test
  void UserStatusOnline_WithoutUser_ShouldThrowException() {
    // given
    UserStatusRequest request = mock(UserStatusRequest.class);
    when(userRepository.findById(userId)).thenReturn(Optional.empty());
    // when & then
    assertThrows(UserNotFoundException.class, () -> {
      userService.online(userId, request);
    });
  }

}
