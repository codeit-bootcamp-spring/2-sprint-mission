package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("BasicUserService 단위 테스트")
class BasicUserServiceTest {

  private void setIdUsingReflection(Object entity, UUID id) {
    Class<?> clazz = entity.getClass();
    while (clazz != null) {
      try {
        Field f = clazz.getDeclaredField("id");
        f.setAccessible(true);
        f.set(entity, id);
        return;
      } catch (NoSuchFieldException ignored) {
        clazz = clazz.getSuperclass();
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    throw new RuntimeException("Cannot find field 'id' on " + entity.getClass());
  }

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStatusService userStatusService;
  @Mock
  private BinaryContentService binaryContentService;
  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService userService;

  private UserCreateRequest userCreateRequest;
  private UserUpdateRequest userUpdateRequest;

  @BeforeEach
  void setUp() {
    userCreateRequest = new UserCreateRequest("testuser", "test@example.com", "password123");
    userUpdateRequest = new UserUpdateRequest("newusername", "new@example.com", "newpassword");

    lenient().when(userMapper.toResponse(any(User.class)))
        .thenAnswer(inv -> {
          User u = inv.getArgument(0);
          BinaryContent profile = u.getProfile();

          BinaryContentResponse binaryContentResponse =
              (profile == null) ? null
                  : new BinaryContentResponse(
                      UUID.fromString("00000000-1111-2222-3333-000000000000"),
                      "testFile",
                      3L,
                      "image/jpeg"
                  );

          return new UserResponse(
              u.getId(),
              u.getUsername(),
              u.getEmail(),
              binaryContentResponse,
              true
          );
        });

    lenient().when(userRepository.save(any(User.class)))
        .thenAnswer(inv -> {
          User u = inv.getArgument(0);
          setIdUsingReflection(u, UUID.fromString("00000000-0000-0000-0000-000000000001"));
          return u;
        });
  }

  @DisplayName("프로필 없이 유저를 생성할 수 있다.")
  @Test
  void create_user_without_profile_success() {
    // given
    given(userRepository.findByUsername(eq(userCreateRequest.username())))
        .willReturn(Optional.empty());
    given(userRepository.findByEmail(eq(userCreateRequest.email())))
        .willReturn(Optional.empty());

    given(userStatusService.create(any(UserStatusCreateRequest.class))).willReturn(null);

    // when
    UserResponse result = userService.create(userCreateRequest, null);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    assertThat(result.username()).isEqualTo(userCreateRequest.username());
    assertThat(result.email()).isEqualTo(userCreateRequest.email());
    assertThat(result.profile()).isNull();
  }

  @DisplayName("프로필 포함하여 유저를 생성할 수 있다.")
  @Test
  void create_user_with_profile_success() {
    // given
    given(userRepository.findByUsername(eq(userCreateRequest.username())))
        .willReturn(Optional.empty());
    given(userRepository.findByEmail(eq(userCreateRequest.email())))
        .willReturn(Optional.empty());

    var profileRequest = new BinaryContentCreateRequest(
        "profile.jpg", "image/jpeg", new byte[]{1, 2, 3}
    );
    var mockContent = new BinaryContent("profile.jpg", 3L, "image/jpeg");
    setIdUsingReflection(mockContent, UUID.fromString("00000000-1111-2222-3333-000000000000"));

    given(binaryContentService.create(eq(profileRequest)))
        .willReturn(mockContent);

    given(userStatusService.create(any(UserStatusCreateRequest.class)))
        .willReturn(null);

    // when
    UserResponse result = userService.create(userCreateRequest, profileRequest);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    assertThat(result.username()).isEqualTo(userCreateRequest.username());
    assertThat(result.email()).isEqualTo(userCreateRequest.email());
    assertThat(result.profile()).isNotNull();
    assertThat(result.profile().id()).isEqualTo(
        UUID.fromString("00000000-1111-2222-3333-000000000000"));
  }

  @DisplayName("중복된 유저 이름으로 유저 생성 시 예외가 발생한다.")
  @Test
  void create_with_duplicate_username_should_throw_exception() {
    // given
    User existingUser = new User("existing", "existing@example.com", "password123");
    setIdUsingReflection(existingUser, UUID.randomUUID());
    given(userRepository.findByUsername(eq(userCreateRequest.username())))
        .willReturn(Optional.of(existingUser));

    // when / then
    assertThatThrownBy(() -> userService.create(userCreateRequest, null))
        .isInstanceOf(DuplicateUsernameException.class);
  }

  @DisplayName("중복된 이메일로 유저 생성 시 예외가 발생한다.")
  @Test
  void create_with_duplicate_email_should_throw_exception() {
    // given
    given(userRepository.findByUsername(eq(userCreateRequest.username())))
        .willReturn(Optional.empty());
    User existingUser = new User("existing", "existing@example.com", "password123");
    setIdUsingReflection(existingUser, UUID.randomUUID());
    given(userRepository.findByEmail(eq(userCreateRequest.email())))
        .willReturn(Optional.of(existingUser));

    // when / then
    assertThatThrownBy(() -> userService.create(userCreateRequest, null))
        .isInstanceOf(DuplicateEmailException.class);
  }

  @DisplayName("유효한 정보로 유저를 수정할 수 있다.")
  @Test
  void update_user_without_profile_success() {
    // given
    User existing = new User("olduser", "old@example.com", "oldpass", null);
    setIdUsingReflection(existing, UUID.fromString("00000000-0000-0000-0000-000000000002"));

    given(userRepository.findById(eq(existing.getId()))).willReturn(Optional.of(existing));
    given(userRepository.existsByUsername(eq(userUpdateRequest.newUsername()))).willReturn(false);
    given(userRepository.existsByEmail(eq(userUpdateRequest.newEmail()))).willReturn(false);

    // when
    UserResponse result = userService.update(existing.getId(), userUpdateRequest, null);

    // then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(existing.getId());
    assertThat(result.username()).isEqualTo(userUpdateRequest.newUsername());
    assertThat(result.email()).isEqualTo(userUpdateRequest.newEmail());
    assertThat(result.profile()).isNull();
  }

  @DisplayName("유저를 삭제할 수 있다")
  @Test
  void delete_user_success() {
    // given
    UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");
    when(userRepository.existsById(id)).thenReturn(true);

    // when / then
    assertThatCode(() -> userService.delete(id)).doesNotThrowAnyException();
  }

  @DisplayName("없는 유저를 삭제하려 하면 예외가 발생한다.")
  @Test
  void delete_user_repository_exception_should_propagate() {
    // given
    when(userRepository.existsById(any(UUID.class))).thenReturn(false);

    // when / then
    assertThatThrownBy(() -> userService.delete(UUID.randomUUID()))
        .isInstanceOf(UserNotFoundException.class);
  }
}