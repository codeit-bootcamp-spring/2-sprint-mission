package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private BasicUserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Test
  void createUser_success() {
    // given
    UserCreateRequest request = new UserCreateRequest("john", "john@email.com", "1234");
    given(userRepository.existsByEmail(any())).willReturn(false);
    given(userRepository.existsByUsername(any())).willReturn(false);

    User user = new User("john", "john@email.com", "1234", null);
    given(userRepository.save(any())).willReturn(user);
    given(userMapper.toDto(any())).willReturn(
        new UserDto(UUID.randomUUID(), "john", "john@email.com", null, true));

    // when
    UserDto result = userService.create(request, Optional.empty());

    // then
    then(userRepository).should(times(1)).save(any());
    assertEquals("john", result.username());
  }

  @Test
  void createUser_fail_duplicateEmail() {
    // given
    UserCreateRequest request = new UserCreateRequest("john", "dup@email.com", "1234");
    given(userRepository.existsByEmail(any())).willReturn(true);

    // when + then
    assertThrows(UserAlreadyExistsException.class, () -> {
      userService.create(request, Optional.empty());
    });
  }
}
