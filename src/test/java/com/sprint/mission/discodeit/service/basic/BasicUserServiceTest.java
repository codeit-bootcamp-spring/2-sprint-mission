package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
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

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

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
}
