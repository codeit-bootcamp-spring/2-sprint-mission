package com.sprint.mission.discodeit.service.basic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {

  @InjectMocks
  private BasicUserService userService;

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


  @Test
  @DisplayName("유저 생성 테스트 - 성공 (profile 포함)")
  void create_success() {
    //given
    UserCreateRequest userCreateRequest = new UserCreateRequest("user1", "user1@email.com", "password");
    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(userRepository.existsByUsername(anyString())).willReturn(false);

    byte[] imageBytes = new byte[]{1, 2, 3};
    BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
        "profile.png", "image/png", imageBytes
    );
    BinaryContent profileImage = new BinaryContent("profile.png", 3L, "image/png");
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(profileImage);
    given(binaryContentStorage.put(any(), any(byte[].class))).willReturn(UUID.randomUUID());  // 테스트 코드에서 생성된 profileImage는 id값을 가지지 않아서 any() 로 받았음... 이거 괜찮나요?

    User user = new User("user1", "user1@email.com", "password", profileImage);
    when(userRepository.save(any(User.class))).thenReturn(user);

    BinaryContentDto profileDto = new BinaryContentDto(UUID.randomUUID(), profileImage.getFileName(), profileImage.getSize(), profileImage.getContentType());
    UserDto userDto = new UserDto(UUID.randomUUID(), "user1", "user1@email.com", profileDto, true);
    when(userMapper.toDto(any(User.class))).thenReturn(userDto);

    //when
    UserDto result = userService.create(userCreateRequest, Optional.of(binaryContentCreateRequest));

    //then
    assertNotNull(result);
    assertEquals("user1", result.username());
    assertEquals("user1@email.com", result.email());

    // verify
    verify(userRepository, times(1)).existsByEmail(anyString());
    verify(userRepository, times(1)).existsByUsername(anyString());
    verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
    verify(binaryContentStorage, times(1)).put(any(), any(byte[].class));
    verify(userRepository, times(1)).save(any(User.class));
    verify(userMapper, times(1)).toDto(any(User.class));
  }

  @Test
  @DisplayName("유저 생성 테스트 - 실패 (중복 이메일)")
  void create_fail_duplicateEmail() {
    //given
    UserCreateRequest request = new UserCreateRequest("user1", "duplicate@email.com", "password");
    given(userRepository.existsByEmail("duplicate@email.com")).willReturn(true);

    //when & then
    DuplicateEmailException exception = assertThrows(
        DuplicateEmailException.class,
        () -> userService.create(request, Optional.empty())
    );

    assertEquals("duplicate@email.com", exception.getDetails().get("email"));
    verify(userRepository, times(1)).existsByEmail("duplicate@email.com");
    verify(userRepository, times(0)).save(any(User.class));
  }

  @Test
  @DisplayName("유저 생성 테스트 - 실패 (중복 유저 이름)")
  void create_fail_duplicateUsername() {
    //given
    UserCreateRequest request = new UserCreateRequest("user1", "duplicate@email.com", "password");
    given(userRepository.existsByUsername("user1")).willReturn(true);

    //when & then
    DuplicateUsernameException exception = assertThrows(
        DuplicateUsernameException.class,
        () -> userService.create(request, Optional.empty())
    );

    assertEquals("user1", exception.getDetails().get("username"));
    verify(userRepository, times(1)).existsByUsername("user1");
    verify(userRepository, times(0)).save(any(User.class));
  }

  @Test
  @DisplayName("유저 업데이트 테스트 - 성공 (profile 포함)")
  void update_success() {
    // given
    UUID userId = UUID.randomUUID();
    User user = new User("user1", "user1@email.com", "password", null);
    given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));

    UserUpdateRequest userUpdateRequest = new UserUpdateRequest("newUser1", "newUser1@email.com", "newPassword");
    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(userRepository.existsByUsername(anyString())).willReturn(false);

    byte[] imageBytes = new byte[]{1, 2, 3, 4};
    BinaryContentCreateRequest updateBinaryContentCreateRequest = new BinaryContentCreateRequest(
        "newProfile.jpg", "image/jpg", imageBytes
    );
    BinaryContent updateProfileImage = new BinaryContent("newProfile.jpg", 4L, "image/jpg");
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(updateProfileImage);
    given(binaryContentStorage.put(any(), any(byte[].class))).willReturn(UUID.randomUUID());  // 테스트 코드에서 생성된 profileImage는 id값을 가지지 않아서 any() 로 받았음... 이거 괜찮나요?

    BinaryContentDto updateProfileDto = new BinaryContentDto(UUID.randomUUID(), updateProfileImage.getFileName(), updateProfileImage.getSize(), updateProfileImage.getContentType());
    UserDto updateUserDto = new UserDto(userId, "user1", "user1@email.com", updateProfileDto, true);
    when(userMapper.toDto(any(User.class))).thenReturn(updateUserDto);

    // when
    UserDto result = userService.update(userId, userUpdateRequest, Optional.of(updateBinaryContentCreateRequest));

    // then
    verify(userRepository, times(1)).findById(any(UUID.class));
    verify(userRepository, times(1)).existsByEmail(anyString());
    verify(userRepository, times(1)).existsByUsername(anyString());
    verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
    verify(binaryContentStorage, times(1)).put(any(), any(byte[].class));
    verify(userMapper, times(1)).toDto(any(User.class));
  }

  @Test
  @DisplayName("유저 업데이트 테스트 - 실패 (유저를 찾을 수 없음)")
  void update_fail_cannot_find_user() {
    // given
    given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    UUID userId = UUID.randomUUID();
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest("newUser1", "newUser1@email.com", "newPassword");


    // when & then
    UserNotFoundException exception = assertThrows(
        UserNotFoundException.class,
        () -> userService.update(userId, userUpdateRequest, Optional.empty())
    );

    // verify
    assertEquals(userId, exception.getDetails().get("userId"));
    verify(userRepository, times(1)).findById(any(UUID.class));
    verify(userRepository, times(0)).existsByEmail(anyString());
  }

  @Test
  @DisplayName("유저 삭제 테스트 - 성공")
  void delete_success() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(any(UUID.class))).willReturn(false);

    // when
    userService.delete(userId);

    // then
    verify(userRepository, times(1)).existsById(any(UUID.class));
    verify(userRepository, times(1)).deleteById(any(UUID.class));
  }

  @Test
  @DisplayName("유저 삭제 테스트 - 실패 (존재하지 않는 유저)")
  void delete_fail_dont_exist_user() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(any(UUID.class))).willReturn(true);

    // when & then
    UserNotFoundException exception = assertThrows(
        UserNotFoundException.class,
        () -> userService.delete(userId)
    );

    // verify
    assertEquals(userId, exception.getDetails().get("userId"));
    verify(userRepository, times(1)).existsById(any(UUID.class));
    verify(userRepository, times(0)).deleteById(any(UUID.class));
  }
}