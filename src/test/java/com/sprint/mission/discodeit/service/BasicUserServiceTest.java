package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

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

        user = new User(username, email, password, null);
        ReflectionTestUtils.setField(user, "id", userId);
        userDto = new UserDto(userId, username, email, null, true);
    }

    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_WithValidInput_Success() {
        //given
        UserCreateRequest request = new UserCreateRequest(username, email, password);
        given(userRepository.existsByEmail(eq(email))).willReturn(false);
        given(userRepository.existsByUsername(eq(username))).willReturn(false);
        given(userMapper.toDto(any(User.class))).willReturn(userDto);

        //when
        UserDto result = userService.create(request, Optional.empty());

        //then
        assertThat(result).isEqualTo(userDto);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 Username로 사용자 생성 시도 시 예외 발생")
    void createUser_WithDuplicateUsername_ThrowException() {
        //given
        UserCreateRequest request = new UserCreateRequest(username, email, password);
        given(userRepository.existsByEmail(eq(email))).willReturn(false);
        given(userRepository.existsByUsername(eq(username))).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.create(request, Optional.empty()))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    @DisplayName("이미 존재하는 email로 사용자 생성 시도 시 예외 발생")
    void createUser_WithDuplicateEmail_ThrowException() {
        //given
        UserCreateRequest request = new UserCreateRequest(username, email, password);
        given(userRepository.existsByEmail(eq(email))).willReturn(true);

        //when & then
        assertThatThrownBy(() -> userService.create(request, Optional.empty()))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    @DisplayName("사용자 조회 성공")
    void findUser_Success() {
        //given
        given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
        given(userMapper.toDto(any(User.class))).willReturn(userDto);

        //when
        UserDto result = userService.find(userId);

        //then
        assertThat(result).isEqualTo(userDto);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 시 실패")
    void findUser_WithInValidUserId_ThrowException() {
        //given
        given(userRepository.findById(eq(userId))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> userService.find(userId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("정상적인 User 업데이트 성공")
    void updateUser_WithValidInput_Success() {
        //given
        String newUsername = "newUsername";
        String newEmail = "newEmail";
        String newPassword = "newPassword";
        UserUpdateRequest request = new UserUpdateRequest(newUsername, newEmail, newPassword);

        given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(eq(newEmail))).willReturn(false);
        given(userRepository.existsByUsername(eq(newUsername))).willReturn(false);
        given(userMapper.toDto(any(User.class))).willReturn(userDto);

        //when
        UserDto result = userService.update(userId, request, Optional.empty());

        //then
        assertThat(result).isEqualTo(userDto);
    }

    @Test
    @DisplayName("존재하지 않는 userId로 업데이트 시 예외 발생")
    void updateUser_WithInvalidUserId_ThrowException() {
        //given
        UserUpdateRequest request = new UserUpdateRequest("newUsername", "new@gmail.com",
                "newPassword");
        given(userRepository.findById(eq(userId))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> userService.update(userId, request, Optional.empty()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("정상적인 User 삭제 성공")
    void deleteUser_WithValidInput_Success() {
        //given
        given(userRepository.existsById(eq(userId))).willReturn(true);

        //when
        userService.delete(userId);

        //then
        verify(userRepository).deleteById(eq(userId));
    }

    @Test
    @DisplayName("존재하지 않는 userId로 삭제 시 예외 발생")
    void deleteUser_WithInvalidUserId_ThrowException() {
        //given
        given(userRepository.existsById(userId)).willReturn(false);

        //when & then
        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserNotFoundException.class);
    }
}