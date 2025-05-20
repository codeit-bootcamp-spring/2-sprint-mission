package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {

    @InjectMocks
    private BasicUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Test
    void 사용자_생성_테스트() {
        // given
        UserCreateRequest request = Mockito.mock(UserCreateRequest.class);

        // when
        userService.save(request, null);

        // then
        then(userRepository).should().save(any());
    }

    @Test
    void 사용자_생성_중_이미_존재하는_사용자_이름_테스트() {
        // given
        UserCreateRequest request = new UserCreateRequest("test", "test", "email@email.com");
        given(userRepository.findByUsername(any())).willReturn(Optional.of(new User()));

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(request, null));
    }

    @Test
    void 사용자_생성_중_이미_존재하는_이메일_테스트() {
        // given
        UserCreateRequest request = new UserCreateRequest("test", "test", "email@email.com");
        given(userRepository.findByEmail(any())).willReturn(Optional.of(new User()));

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(request, null));
    }

    @Test
    void 사용자_수정_테스트() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User();
        UserUpdateRequest request = new UserUpdateRequest("test", "test", "email@email.com");
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.saveAndFlush(any())).willReturn(user);

        // when
        userService.update(userId, request, null);

        // then
        assertThat(user.getUsername()).isEqualTo("test");
        assertThat(user.getPassword()).isEqualTo("test");
        assertThat(user.getEmail()).isEqualTo("email@email.com");
    }

    @Test
    void 사용자_수정_중_대상_사용자_찾을_수_없음_테스트() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = Mockito.mock(UserUpdateRequest.class);
        given(userRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.update(userId, request, null));
    }

    @Test
    void 사용자_수정_중_중복된_사용자_이름_테스트() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("test", "test", "email@email.com");
        User user = new User();
        Field userIdField = BaseEntity.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(user, userId);

        given(userRepository.findById(any())).willReturn(Optional.of(new User()));
        given(userRepository.findByUsername(any())).willReturn(Optional.of(user));

        // when & then
        assertThrows(UserAlreadyExistsException.class,
            () -> userService.update(userId, request, null));
    }

    @Test
    void 사용자_수정_중_중복된_이메일_테스트() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("test", "test", "email@email.com");
        User user = new User();
        Field userIdField = BaseEntity.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(user, userId);

        given(userRepository.findById(any())).willReturn(Optional.of(new User()));
        given(userRepository.findByEmail(any())).willReturn(Optional.of(user));

        // when & then
        assertThrows(UserAlreadyExistsException.class,
            () -> userService.update(userId, request, null));
    }

    @Test
    void 사용자_삭제_테스트() {
        // given
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(any())).willReturn(Optional.of(new User()));

        // when
        userService.delete(userId);

        // then
        then(userRepository).should().delete(any());
    }

    @Test
    void 사용자_삭제_시_사용자_찾을_수_없음_테스트() {
        // given
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
    }
}
