package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserDuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
public class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;

  @InjectMocks
  private BasicUserService basicUserService;

  UUID dummyId = UUID.randomUUID();
  String dummyUsername = "testuser";
  String dummyEmail = "test@example.com";
  BinaryContentDto dummyProfile = new BinaryContentDto(
      UUID.randomUUID(),
      "profile.jpg",
      1024L,
      "image/jpeg"
  );
  Boolean dummyOnline = true;

  // CREATE METHOD TESTS ==============================================
  @Test
  void createUser_Success_WithoutProfile() {
    // Given
    UserCreateRequest request = new UserCreateRequest(dummyUsername, dummyEmail, "password");
    given(userRepository.existsByEmail(dummyEmail)).willReturn(false);
    given(userRepository.existsByUsername(dummyUsername)).willReturn(false);
    given(userMapper.toDto(any(User.class))).willReturn(
        new UserDto(dummyId, dummyUsername, dummyEmail, dummyProfile, dummyOnline));

    // When
    UserDto result = basicUserService.create(request, Optional.empty());

    // Then
    assertNotNull(result);
    then(userRepository).should().save(any(User.class));
  }

  @Test
  void createUser_Fail_DuplicateEmail() {
    // Given
    UserCreateRequest request = new UserCreateRequest(dummyUsername, dummyEmail, "password");
    given(userRepository.existsByEmail(dummyEmail)).willReturn(true);

    // When & Then
    assertThrows(UserDuplicateEmailException.class, () -> {
      basicUserService.create(request, Optional.empty());
    });
  }

  // UPDATE METHOD TESTS ==============================================
  @Test
  void updateUser_Success_WithProfileImage() {
    // Given
    UserUpdateRequest request = new UserUpdateRequest("newuser", "new@example.com", "newpass");
    BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest(
        "profile.jpg", "image/jpeg", new byte[1024]
    );

    User existingUser = new User(dummyUsername, dummyEmail, "oldpass", null);
    given(userRepository.findById(dummyId)).willReturn(Optional.of(existingUser));
    given(userRepository.existsByEmail("new@example.com")).willReturn(false);
    given(userRepository.existsByUsername("newuser")).willReturn(false);
    given(binaryContentRepository.save(any())).willReturn(
        new BinaryContent("profile.jpg", 1024L, "image/jpeg"));

    // When
    basicUserService.update(dummyId, request, Optional.of(profileRequest));

    // Then
    then(binaryContentStorage).should().put(any(), any());
    assertEquals("newuser", existingUser.getUsername());
  }

  @Test
  void updateUser_Fail_UserNotFound() {
    // Given
    given(userRepository.findById(dummyId)).willReturn(Optional.empty());

    // When & Then
    assertThrows(UserNotFoundException.class, () -> {
      basicUserService.update(dummyId,
          new UserUpdateRequest("newuser", "new@example.com", "newpassword"),
          Optional.empty());
    });
  }

  // DELETE METHOD TESTS ==============================================
  @Test
  void deleteUser_Success() {
    // Given
    given(userRepository.existsById(dummyId)).willReturn(true);

    // When
    basicUserService.delete(dummyId);

    // Then
    then(userRepository).should().deleteById(dummyId);
  }

  @Test
  void deleteUser_Fail_NotFound() {
    // Given
    given(userRepository.existsById(dummyId)).willReturn(false);

    // When & Then
    assertThrows(UserNotFoundException.class, () -> {
      basicUserService.delete(dummyId);
    });
  }
}