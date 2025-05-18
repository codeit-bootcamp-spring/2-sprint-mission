package com.sprint.mission.discodeit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusRepository userStatusRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private UserMapper userMapper;

  @Test
  @DisplayName("프로필 이미지가 없는 정상 유저 생성 테스트")
  void createUser() {
    UserCreateRequest userCreateRequest = new UserCreateRequest("테스트유저", "test@test.com",
        "test123");

    userService.create(userCreateRequest, Optional.empty());

    then(userRepository).should().save(any(User.class));
  }

  @Test
  @DisplayName("프로필 이미지가 있는 정상 유저 생성 테스트")
  void createUserWithProfile() {
    UserCreateRequest userCreateRequest = new UserCreateRequest("테스트유저", "test@test.com",
        "test123");
    BinaryContentCreateRequest profile = new BinaryContentCreateRequest("file.png", "image/png",
        new byte[]{1, 2, 3});

    userService.create(userCreateRequest, Optional.of(profile));

    then(userRepository).should().save(any(User.class));
    then(binaryContentRepository).should().save(any());
    then(binaryContentStorage).should().put(any(), any());
  }

  @Test
  @DisplayName("이메일 중복으로 인한 유저 생성 실패")
  void failIfEmailExists() {
    UserCreateRequest request = new UserCreateRequest("유저", "duplicate@test.com", "pw123");

    given(userRepository.existsByEmail("duplicate@test.com")).willReturn(true);

    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(EmailAlreadyExistsException.class);
  }

  @Test
  @DisplayName("유저네임 중복으로 인한 유저 생성 실패")
  void failIfUsernameExists() {
    UserCreateRequest request = new UserCreateRequest("중복유저", "new@test.com", "pw123");

    given(userRepository.existsByEmail("new@test.com")).willReturn(false);
    given(userRepository.existsByUsername("중복유저")).willReturn(true);

    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class);
  }
}
