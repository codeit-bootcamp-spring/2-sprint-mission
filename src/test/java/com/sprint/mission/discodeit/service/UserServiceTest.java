package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.User.UserAlreadyExistException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;

import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    @Test
    void createUser_nullProfile_success() {
        // given
        UUID userId = UUID.randomUUID();
        String username = "test";
        String password = "1234";
        String email = "test@test.com";
        UserCreateRequest request = new UserCreateRequest(username, email, password);

        User user = new User(username, email, password, null);
        UserDto expectedDto = new UserDto(userId, username, email, null, false);

        given(userRepository.existsByEmail(email)).willReturn(false);
        given(userRepository.existsByUsername(username)).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userMapper.toDto(any(User.class))).willReturn(expectedDto);


        // when
        UserDto userDto = userService.create(request, Optional.empty());

        // then
        assertThat(userDto.id()).isEqualTo(userId);
        assertThat(userDto.username()).isEqualTo(username);
        assertThat(userDto.email()).isEqualTo(email);
        assertThat(userDto.profile()).isNull();
        assertThat(userDto.online()).isFalse();

        then(userRepository).should().existsByEmail(email);
        then(userRepository).should().existsByUsername(username);
        then(userRepository).should().save(any(User.class));
        then(userMapper).should().toDto(any(User.class));
    }

    @Test
    void createUser_fail_duplicateEmail() {
        // given
        String username = "test";
        String password = "1234";
        String email = "test@test.com";
        UserCreateRequest request = new UserCreateRequest(username, email, password);

        given(userRepository.existsByEmail(email)).willReturn(true);

        // when & then
        assertThrows(UserAlreadyExistException.class, () -> userService.create(request, null));
        then(userRepository).should().existsByEmail(email);
        then(userRepository).shouldHaveNoMoreInteractions();
    }


}