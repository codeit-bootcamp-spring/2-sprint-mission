package com.sprint.mission.discodeit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserInvalidRequestException;
import com.sprint.mission.discodeit.core.user.exception.UserLoginFailedException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.BasicUserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

  @Mock
  private JpaUserRepository userRepository;

  @Mock
  private UserStatusService userStatusService;

  @Mock
  private BinaryContentService binaryContentService;

  @InjectMocks
  private BasicUserService userService;

  private User user;
  private UUID userId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    userService = new BasicUserService(userRepository, userStatusService, binaryContentService);
    BinaryContent oldProfile = BinaryContent.create("old.png", 0L, "image/png");
    user = User.create("a", "a@email.com", "a", oldProfile);
    user.setUserStatus(UserStatus.create(user, Instant.now()));
  }

  @Test
  void UserCreateTestSuccess() {
    CreateUserCommand command = new CreateUserCommand("test", "test@test.com", "test");
    // given
    UserResult result = userService.create(command, Optional.empty());
    // when & then
    assertThat(result.username()).isEqualTo("test");
    assertThat(result.email()).isEqualTo("test@test.com");
    assertNull(result.profile());
    assertTrue(result.online());
  }

  @Test
  void CreateUser_WithSameNameAndEmail_ShouldThrowException() {
    // given
    CreateUserCommand command = new CreateUserCommand("test", "test@test.com", "test");
    // when
    when(userRepository.existsByName(command.username())).thenReturn(true);
    // then
    assertThrows(UserAlreadyExistsException.class, () -> {
      userService.create(command, Optional.empty());
    });
  }

  @Test
  void CreateUser_WithoutName_ShouldThrowException() {
    // given
    CreateUserCommand noNameCommand = new CreateUserCommand(null, "test@test.com", "test");
    // when & then
    assertThrows(UserInvalidRequestException.class, () -> {
      userService.create(noNameCommand, Optional.empty());
    });
  }

  @Test
  void CreateUser_WithoutEmail_ShouldThrowException() {
    // given
    CreateUserCommand noEmailCommand = new CreateUserCommand("test", null, "test");
    // when & then
    assertThrows(UserInvalidRequestException.class, () -> {
      userService.create(noEmailCommand, Optional.empty());
    });
  }

  @Test
  void UserUpdateTest() {
    // given
    UUID userId = UUID.randomUUID();
    UpdateUserCommand command = new UpdateUserCommand(userId, "newName", "newEmail", "newPassword");
    CreateBinaryContentCommand contentCommand = new CreateBinaryContentCommand(null, null, null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(binaryContentService.create(contentCommand)).thenReturn(null);
    when(userRepository.save(user)).thenReturn(user);

    // when
    UserResult result = userService.update(command, Optional.of(contentCommand));

    // then
    assertThat(result.username()).isEqualTo("newName");
    then(binaryContentService).should().delete(any());
    then(userRepository).should().save(user);
  }

  @Test
  void UserLoginTestSuccess() {
    // given
    LoginUserCommand loginUserCommand = new LoginUserCommand("a", "a");
    when(userRepository.findByName("a")).thenReturn(Optional.of(user));
    // when
    UserResult result = userService.login(loginUserCommand);
    // then
    assertThat(result.username()).isEqualTo("a");
  }

  @Test
  void UserLogin_WithWrongName_ShouldThrowException() {
    // given
    LoginUserCommand loginUserCommand = new LoginUserCommand("b", "a");
    // when & then
    assertThrows(UserNotFoundException.class, () -> {
      userService.login(loginUserCommand);
    });
  }

  @Test
  void UserLogin_WithWrongPassword_ShouldThrowException() {
    // given
    LoginUserCommand loginUserCommand = new LoginUserCommand("a", "b");
    when(userRepository.findByName("a")).thenReturn(Optional.of(user));
    // when & then
    assertThrows(UserLoginFailedException.class, () -> {
      userService.login(loginUserCommand);
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
}
