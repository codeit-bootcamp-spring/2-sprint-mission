package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

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
  @InjectMocks
  private BasicUserService userService;

  @Test
  void create_success_no_profile() {
    UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "1234");
    UUID uuid = UUID.randomUUID();

    given(userRepository.existsByEmail("test@test.com")).willReturn(false);
    given(userRepository.existsByUsername("test")).willReturn(false);
    given(userMapper.toDto(any())).willReturn(
        new UserDto(uuid, request.username(), request.email(), null, true)
    );

    UserDto result = userService.create(request, Optional.empty());

    then(userRepository).should(times(1)).save(any());

    assertNotNull(result);
    assertEquals("test", result.username());
    assertEquals("test@test.com", result.email());
    assertTrue(result.online());
  }

  @Test
  void create_fail_duplicate_name() {
    UserCreateRequest request = new UserCreateRequest("test", "test@test.com", "1234");

    given(userRepository.existsByEmail("test@test.com")).willReturn(false);
    given(userRepository.existsByUsername("test")).willReturn(true);

    UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () ->
        userService.create(request, Optional.empty())
    );

    then(userRepository).should(never()).save(any());

    assertTrue(exception.getMessage().contains("이미 존재하는 사용자입니다."));
  }

  @Test
  void delete_success() {
    UUID uuid = UUID.randomUUID();

    given(userRepository.existsById(uuid)).willReturn(true);

    userService.delete(uuid);

    then(userRepository).should().existsById(uuid);
    then(userRepository).should().deleteById(uuid);
  }

  @Test
  void delete_fail() {
    UUID uuid = UUID.randomUUID();

    given(userRepository.existsById(uuid)).willReturn(false);

    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      userService.delete(uuid);
    });

    then(userRepository).should(never()).deleteById(uuid);

    assertTrue(exception.getMessage().contains("유저를 찾을 수 없습니다."));
  }
}