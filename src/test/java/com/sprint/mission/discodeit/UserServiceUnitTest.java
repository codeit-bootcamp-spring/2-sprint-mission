package com.sprint.mission.discodeit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserInvalidRequestException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.BasicUserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

  private CreateUserCommand command;

  @BeforeEach
  void setUp() {
    userService = new BasicUserService(userRepository, userStatusService, binaryContentService);
    command = new CreateUserCommand("test", "test@test.com", "test");
  }

  @Test
  @DisplayName("Successful User Creation Test")
  void UserCreateTestSuccess() {
    // given
    UserResult result = userService.create(command, Optional.empty());
    // when & then
    assertThat(result.username()).isEqualTo("test");
    assertThat(result.email()).isEqualTo("test@test.com");
    assertNull(result.profile());
    assertTrue(result.online());
  }

  @Test
  @DisplayName("When trying to create a User With Same name and email")
  void CreateUser_WithSameNameAndEmail_ShouldThrowException() {
    // given
    CreateUserCommand testCommand = new CreateUserCommand("test", "test@test.com", "test");
    // when
    userService.create(command, Optional.empty());
    when(userRepository.existsByEmail(command.email())).thenReturn(true);
    // then
    assertThrows(UserAlreadyExistsException.class, () -> {
      userService.create(testCommand, Optional.empty());
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
  void UserUpdateTestSuccess() {
    // given

    // when

    // then

  }

  @Test
  void UserUpdateTestFail() {
    // given

    // when

    // then

  }

  @Test
  void UserDeleteTestSuccess() {
    // given

    // when

    // then

  }

  @Test
  void UserDeleteTestFail() {
    // given

    // when

    // then

  }
}
