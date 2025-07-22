package com.sprint.mission.discodeit.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.core.storage.service.BinaryContentService;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.service.BasicUserService;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

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

  private UUID userId;
  private String username;
  private String email;
  private String password;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    username = "testUser";
    email = "test@example.com";
    password = "password123";

    user = User.create(username, email, password, null);
    ReflectionTestUtils.setField(user, "id", userId);
    userDto = UserDto.from(user);
  }

  @Test
  void 유저생성() throws IOException {
    // given
    UserCreateRequest request = new UserCreateRequest(username, email, password);
    given(userRepository.save(any(User.class))).willAnswer(invocation -> {
      User userToSave = invocation.getArgument(0);
      // save 메소드에 전달된 User 객체에 우리가 원하는 ID를 강제로 주입합니다.
      ReflectionTestUtils.setField(userToSave, "id", userId);
      // ID가 주입된 User 객체를 반환합니다.
      return userToSave;
    });
    // when
    UserDto result = userService.create(request, Optional.empty());
    // then
    assertThat(result).isEqualTo(userDto);
    verify(userRepository).save(any(User.class));
  }
}
