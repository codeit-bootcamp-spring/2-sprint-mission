package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateUserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.Collections;
import java.util.List;
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
public class BasicUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentService binaryContentService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService basicUserService;

    private CreateUserRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        request = new CreateUserRequest("testuser", "password123",
            "test@example.com", null, null);
        user = new User("testuser", "password123", "test@example.com", null);
    }

    @Test
    @DisplayName("createUser(): 중복이 없으면 새 사용자를 생성")
    void createUser_success() {
        // given
        given(userRepository.findAll()).willReturn(Collections.emptyList());
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userMapper.toDto(any(User.class))).willReturn(
            new UserDto(UUID.randomUUID(), "testuser", "test@example.com", null, true)
        );

        // when
        UserDto result = basicUserService.createUser(request, Optional.empty());

        // then
        assertThat(result.username()).isEqualTo("testuser");
        then(userRepository).should().save(any(User.class));
        then(userStatusRepository).should().save(any(UserStatus.class));
    }

    @Test
    @DisplayName("createUser(): 사용자명 또는 이메일이 중복되면 예외 던짐")
    void createUser_fail_duplicateUser() {
        // given
        given(userRepository.findAll()).willReturn(List.of(user));

        // when & then
        assertThrows(DuplicateUserException.class, () -> {
            basicUserService.createUser(request, Optional.empty());
        });
    }

    @Test
    @DisplayName("updateUser(): 존재하는 사용자 정보를 성공적으로 수정")
    void updateUser_success() {
        // given
        UUID userId = UUID.randomUUID();
        UpdateUserRequest reqeust = new UpdateUserRequest(userId, "newName",
            "newPass", "new@email.com");
        User existingUser = new User("oldName", "oldPass", "old@email.com", null);

        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(userRepository.existsByUsername("newName")).willReturn(false);
        given(userRepository.existsByEmail("new@email.com")).willReturn(false);
        given(userMapper.toDto(existingUser)).willReturn(
            new UserDto(userId, "newName", "new@email.com", null, true));

        // when
        UserDto result = basicUserService.updateUser(reqeust, Optional.empty());

        // then
        assertThat(result.username()).isEqualTo("newName");
        then(userRepository).should().findById(userId);
    }

    @Test
    @DisplayName("updateUser(): userId에 해당하는 사용자가 존재하지 않으면 예외를 던짐")
    void updateUser_fail_userNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest(userId, "newName",
            "newPass", "new@email.com");

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> {
            basicUserService.updateUser(request, Optional.empty());
        });
    }

    @Test
    @DisplayName("updateUser(): 중복된 username 또는 email이면 예외를 던짐")
    void updateUser_fail_duplicateUsernameOrEmail() {
        // given
        UUID userId = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest(userId, "dupName",
            "pass", "dup@email.com");
        User existingUser = new User("oldName", "oldPass", "old@email.com", null);

        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(userRepository.existsByUsername("dupName")).willReturn(true);

        // when & then
        assertThrows(DuplicateUserException.class, () -> {
            basicUserService.updateUser(request, Optional.empty());
        });
    }

    @Test
    @DisplayName("deleteUser(): 존재하는 사용자를 삭제한다")
    void deleteUser_success() {
        // given
        UUID userId = UUID.randomUUID();
        BinaryContent profile = new BinaryContent("profile.png", 123L, "image/png");
        User user = new User("user", "pass", "email@test.com", profile);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        basicUserService.deleteUser(userId);

        // then
        then(binaryContentRepository).should().delete(profile);
        then(userRepository).should().delete(user);
    }

    @Test
    @DisplayName("deleteUser(): userId에 해당하는 사용자가 없으면 예외를 던짐")
    void deleteUser_fail_userNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> {
            basicUserService.deleteUser(userId);
        });
    }
}
