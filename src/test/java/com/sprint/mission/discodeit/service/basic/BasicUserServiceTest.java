package com.sprint.mission.discodeit.service.basic;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.service.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserDto;
import com.sprint.mission.discodeit.dto.service.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private BasicUserService basicUserService;

  private User user;
  private UUID id;
  private String username;
  private String email;
  private String password;
  private UserDto userDto;


  @BeforeEach
  public void setUp() {
    id = UUID.randomUUID();
    username = "discord";
    email = "discord@gmail.com";
    password = "discord1234";

    user = new User(username, email, password, null);
    ReflectionTestUtils.setField(user, "id", id);
    userDto = userMapper.toDto(user);
  }

  @Test
  public void createUserByExistingUsername_throwUserAlreadyExistException() {
    given(userRepository.existsByUsername(eq(username))).willReturn(true);
    UserCreateRequest request = new UserCreateRequest(username, email, password);

    assertThatThrownBy(() -> basicUserService.create(request, null))
        .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  public void createUserByExistingEmail_throwUserAlreadyExistException() {
    given(userRepository.existsByEmail(eq(email))).willReturn(true);
    UserCreateRequest request = new UserCreateRequest(username, email, password);

    assertThatThrownBy(() -> basicUserService.create(request, null))
        .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  public void createUser_success() {
    given(userRepository.existsByUsername(eq(username))).willReturn(false);
    given(userRepository.existsByEmail(eq(email))).willReturn(false);
    UserCreateRequest request = new UserCreateRequest(username, email, password);

    UserDto dto = basicUserService.create(request, null);
    then(dto).isEqualTo(userDto);
    verify(userRepository).save(any(User.class));
    verify(userMapper).toDto(user);
  }

  @Test
  public void updateUserById_userNotFound_throwsUserNotFoundException() {
    given(userRepository.findByIdWithProfileAndUserStatus(id)).willReturn(Optional.empty());
    UserUpdateRequest request = new UserUpdateRequest(username, email, password);

    assertThatThrownBy(() -> basicUserService.update(id, request, null))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  public void updateUser_success() {
    UserUpdateRequest request = new UserUpdateRequest("newdiscord", email, password);
    given(userRepository.findByIdWithProfileAndUserStatus(id)).willReturn(Optional.of(user));
    given(userRepository.existsByUsername(eq("newdiscord"))).willReturn(false);

    UserDto dto = basicUserService.update(id, request, null);
    then(dto).isEqualTo(userDto);
  }

  @Test
  public void deleteUserById_userNotFound_throwsUserNotFoundException() {
    given(userRepository.existsById(id)).willReturn(false);

    assertThatThrownBy(() -> basicUserService.delete(id))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  public void deleteUser_success() {
    given(userRepository.existsById(id)).willReturn(true);

    basicUserService.delete(id);
    verify(userRepository).deleteById(id);
  }
}
