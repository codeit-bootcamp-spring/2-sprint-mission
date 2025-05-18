package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserEmailExistsException;
import com.sprint.mission.discodeit.exception.user.UserNameExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
@DisplayName("BasicUserService Test")
public class BasicUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    @Test
    @DisplayName("유저 생성 성공 테스트")
    void createUser_success() {
        // given
        UserCreateDto dto = new UserCreateDto("test", "test1234", "test@gmail.com");
        User user = new User(dto.username(), dto.email(), dto.password(), null);
        UserDto expectedUserDto = new UserDto(user, null, false);

        given(userRepository.existsByEmail(dto.email())).willReturn(false);
        given(userRepository.existsByUsername(dto.username())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userMapper.toDto(any(User.class))).willReturn(expectedUserDto);

        // when
        UserDto result = userService.create(dto, null);

        // then
        assertThat(result).isEqualTo(expectedUserDto);
    }

    @Test
    @DisplayName("유저 중복 이메일 테스트")
    void createUser_fail_whenEmailExists() {
        // given
        UserCreateDto dto = new UserCreateDto("test", "test1234", "test@gmail.com");
        given(userRepository.existsByEmail(dto.email())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> userService.create(dto, null))
                .isInstanceOf(UserEmailExistsException.class);
    }

    @Test
    @DisplayName("유저 중복 이름 테스트")
    void createUser_fail_whenUsernameExists() {
        // given
        UserCreateDto dto = new UserCreateDto("test", "test1234", "test@gmail.com");
        given(userRepository.existsByUsername(dto.username())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> userService.create(dto, null))
                .isInstanceOf(UserNameExistsException.class);
    }

    @Test
    @DisplayName("프로필을 포함한 유저 생성 성공 테스트")
    void createUser_success_withProfile() {
        // given
        UserCreateDto dto = new UserCreateDto("test", "test1234", "test@gmail.com");
        BinaryContentCreateDto profileDto = new BinaryContentCreateDto("profile.jpg", "image/jpeg", new byte[10]);
        User user = new User(dto.username(), dto.email(), dto.password(), null);
        UserDto expectedUserDto = new UserDto(user, null, false);

        given(userRepository.existsByEmail(dto.email())).willReturn(false);
        given(userRepository.existsByUsername(dto.username())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userMapper.toDto(any(User.class))).willReturn(expectedUserDto);

        // when
        UserDto result = userService.create(dto, profileDto);

        // then
        assertThat(result).isEqualTo(expectedUserDto);
        then(binaryContentRepository).should().save(any());
        then(binaryContentStorage).should().put(any(), any());
    }

    @Test
    @DisplayName("프로필을 포함하지 않은 유저 수정 성공 테스트")
    void updateUser_success_withoutProfile() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateDto updateDto = new UserUpdateDto("newName", "new@gmail.com", "newPassword");
        User user = new User("oldName", "old@gmail.com", "oldPassword", null);
        UserDto expectedUserDto = new UserDto(user, null, false);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(updateDto.newEmail())).willReturn(false);
        given(userRepository.existsByUsername(updateDto.newUsername())).willReturn(false);
        given(userMapper.toDto(user)).willReturn(expectedUserDto);

        // when
        UserDto result = userService.update(userId, updateDto, null);

        // then
        assertThat(result).isEqualTo(expectedUserDto);
        then(binaryContentRepository).shouldHaveNoInteractions();
        then(binaryContentStorage).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("프로필을 포함한 유저 수정 성공 테스트")
    void updateUser_success_withProfile() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateDto updateDto = new UserUpdateDto("newName", "new@gmail.com", "newPassword");
        User user = new User("oldName", "old@gmail.com", "oldPassword", null);
        UserDto expectedDto = new UserDto(user, null, false);
        BinaryContentCreateDto profileDto = new BinaryContentCreateDto("file.jpg", "image/jpeg", new byte[20]);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(updateDto.newEmail())).willReturn(false);
        given(userRepository.existsByUsername(updateDto.newUsername())).willReturn(false);
        given(userMapper.toDto(user)).willReturn(expectedDto);

        // when
        UserDto result = userService.update(userId, updateDto, profileDto);

        // then
        assertThat(result).isEqualTo(expectedDto);
        then(binaryContentRepository).should().save(any());
        then(binaryContentStorage).should().put(any(), any());
    }

    @Test
    @DisplayName("유저 수정 실패 - 유저를 찾을 수 없음")
    void updateUser_fail_whenUserNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateDto updateDto = new UserUpdateDto("newName", "new@gmail.com", "newPassword");

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> userService.update(userId, updateDto, null))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("유저 수정 실패 - 이메일 중복")
    void updateUser_fail_whenEmailExists() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateDto updateDto = new UserUpdateDto("newName", "new@gmail.com", "newPassword");
        User user = new User("oldName", "old@gmail.com", "oldPassword", null);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(updateDto.newEmail())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> userService.update(userId, updateDto, null))
                .isInstanceOf(UserEmailExistsException.class);
    }

    @Test
    void updateUser_fail_whenUsernameExists() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateDto updateDto = new UserUpdateDto("newName", "new@gmail.com", "newPassword");
        User user = new User("oldName", "old@gmail.com", "oldPassword", null);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(updateDto.newEmail())).willReturn(false);
        given(userRepository.existsByUsername(updateDto.newUsername())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> userService.update(userId, updateDto, null))
                .isInstanceOf(UserNameExistsException.class);
    }

    @Test
    void updateUser_callsUserUpdateMethod() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateDto updateDto = new UserUpdateDto("newName", "new@gmail.com", "newPassword");
        User user = spy(new User("oldName", "old@gmail.com", "oldPassword", null));
        UserDto expectedDto = new UserDto(user, null, false);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(updateDto.newEmail())).willReturn(false);
        given(userRepository.existsByUsername(updateDto.newUsername())).willReturn(false);
        given(userMapper.toDto(user)).willReturn(expectedDto);

        // when
        userService.update(userId, updateDto, null);

        // then
        then(user).should().update(
                eq(updateDto.newUsername()),
                eq(updateDto.newEmail()),
                eq(updateDto.newPassword()),
                isNull()
        );
    }

    @Test
    @DisplayName("유저 삭제 성공 테스트")
    void deleteUser_success() {
        // given
        UUID userId = UUID.randomUUID();
        User mockUser = mock(User.class);
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

        // when
        userService.delete(userId);

        // then
        then(userRepository).should().delete(mockUser);
    }

    @Test
    @DisplayName("유저 삭제 실패 - 유저를 찾을 수 없음")
    void deleteUser_fail_whenUserNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserNotFoundException.class);

        then(userRepository).should(never()).delete(any());
    }
}
