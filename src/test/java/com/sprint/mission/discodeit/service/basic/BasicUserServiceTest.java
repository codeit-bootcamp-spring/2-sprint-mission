package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.exception.user.DuplicateUserException;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private BinaryContentRepository binaryContentRepository;
    @Mock private BinaryContentStorage binaryContentStorage;
    @Mock private UserStatusRepository userStatusRepository;
    @Mock private UserMapper userMapper;
    @Mock private BinaryContentMapper binaryContentMapper;

    @InjectMocks
    private BasicUserService basicUserService;

    @Test
    void create_성공_프로필_없음() {
        //given
        UserCreateRequest request = new UserCreateRequest("testuser", "test@test.com",
                "password123");
        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

        User requestedUser = new User(request.username(), request.email(), request.password(), null);
        UUID userId = UUID.randomUUID();
        setId(requestedUser, userId);

        UserDto expectedDto = new UserDto(userId, request.username(), request.email(), null, true);

        given(userRepository.existsByEmail(request.email())).willReturn(false);
        given(userRepository.existsByUsername(request.username())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(requestedUser);
        given(userMapper.toDto(any(User.class))).willReturn(expectedDto);

        //when
        UserDto result = basicUserService.create(request, profileRequest);

        //then
        assertThat(result).isEqualTo(expectedDto);
        then(userRepository).should(times(1)).save(any(User.class));

    }

    @Test
    void create_성공_프로필_있음() {
        //given
        UserCreateRequest request = new UserCreateRequest("testuser", "test@test.com",
                "password123");
        BinaryContentCreateRequest profileContent = new BinaryContentCreateRequest(
                "profile.jpg", "image/jpeg", new byte[]{1, 2, 3});
        Optional<BinaryContentCreateRequest> profileRequest = Optional.of(profileContent);

        BinaryContent binaryContent = new BinaryContent(profileContent.fileName(),
                (long) profileContent.bytes().length, "image/jpeg");
        UUID profileId = UUID.randomUUID();

        User requestedUser = new User(request.username(), request.email(), request.password(),
                binaryContent);
        UUID userId = UUID.randomUUID();
        setId(requestedUser, userId);

        UserDto expectedDto = new UserDto(userId, request.username(), request.email(), null, true);

        given(userRepository.existsByEmail(request.email())).willReturn(false);
        given(userRepository.existsByUsername(request.username())).willReturn(false);
        given(binaryContentRepository.save(any(BinaryContent.class))).willAnswer(invocation -> {
            BinaryContent savedContent = invocation.getArgument(0);
            setId(savedContent, profileId);
            return savedContent;
        });

        given(userRepository.save(any(User.class))).willReturn(requestedUser);
        given(userMapper.toDto(any(User.class))).willReturn(expectedDto);

        //when
        UserDto result = basicUserService.create(request, profileRequest);

        //then
        assertThat(result).isEqualTo(expectedDto);
        then(binaryContentRepository).should(times(1)).save(any(BinaryContent.class));
        then(binaryContentStorage).should(times(1)).put(profileId, profileContent.bytes());
        then(userRepository).should(times(1)).save(any(User.class));

    }

    @Test
    void create_실패_이메일_중복() {
        //given
        UserCreateRequest request = new UserCreateRequest("testuser", "test@test.com", "password123");
        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

        given(userRepository.existsByEmail(request.email())).willReturn(true);

        // 이메일 중복 예외 발생 검증
        assertThatThrownBy(() -> basicUserService.create(request, profileRequest))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("email");

        then(userRepository).should(times(0)).save(any(User.class));
    }

    @Test
    void create_실패_사용자명_중복() {
        //given
        UserCreateRequest request = new UserCreateRequest("testuser", "test@test.com", "password123");
        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

        given(userRepository.existsByEmail(request.email())).willReturn(false);
        given(userRepository.existsByUsername(request.username())).willReturn(true);

        // 사용자명 중복 예외 발생 검증
        assertThatThrownBy(() -> basicUserService.create(request, profileRequest))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("username");

        then(userRepository).should(times(0)).save(any(User.class));
    }

    private void setId(Object entity, UUID id) {
        try {
            if (entity instanceof BaseUpdatableEntity) {
                Field idField = entity.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, id);
            } else {
                Field idField = entity.getClass().getSuperclass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, id);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("엔티티 ID 설정 중 오류 발생", e);
        }
    }
}
