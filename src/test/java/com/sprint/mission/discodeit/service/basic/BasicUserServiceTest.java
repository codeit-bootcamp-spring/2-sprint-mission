package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class BasicUserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BasicUserServiceTest.class);

    @InjectMocks
    private BasicUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 사용자_생성_성공() {
        // given
        UserCreateRequest request = new UserCreateRequest("username", "password", "email@test.com");
        MockMultipartFile profile = new MockMultipartFile("profile", "profile.png", "image/png",
            "fake-content".getBytes());
        UUID userId = UUID.randomUUID();
        UUID binaryContentId = UUID.randomUUID();

        given(userRepository.findByUsername("username")).willReturn(Optional.empty());
        given(userRepository.findByEmail("email@test.com")).willReturn(Optional.empty());

        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            Field userIdField = BaseEntity.class.getDeclaredField("id");
            userIdField.setAccessible(true);
            userIdField.set(saved, userId);

            if (saved.getProfile() != null) {
                Field profileIdField = BaseEntity.class.getDeclaredField("id");
                profileIdField.setAccessible(true);
                profileIdField.set(saved.getProfile(), binaryContentId);
            }
            return saved;
        });

        given(userMapper.toDto(any(User.class))).willAnswer(invocation -> {
            User user = invocation.getArgument(0);
            BinaryContent profileEntity = user.getProfile();
            BinaryContentDto profileDto = new BinaryContentDto(
                profileEntity.getId(),
                profileEntity.getFileName(),
                profileEntity.getSize(),
                profileEntity.getContentType(),
                profile.getBytes()
            );
            return new UserDto(user.getId(), user.getUsername(), user.getEmail(), profileDto,
                user.getStatus().isLastStatus());
        });

        // when
        UserDto result = userService.save(request, profile);

        // then
        then(userRepository).should().save(any(User.class));
        then(userMapper).should().toDto(any(User.class));

        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.username()).isEqualTo("username");
        assertThat(result.email()).isEqualTo("email@test.com");

        assertThat(result.profile().id()).isEqualTo(binaryContentId);
        assertThat(result.profile().fileName()).isEqualTo("profile.png");
        assertThat(result.profile().contentType()).isEqualTo("image/png");
        assertThat(result.profile().size()).isEqualTo("fake-content".getBytes().length);
        assertThat(result.profile().bytes()).isEqualTo("fake-content".getBytes());

        assertThat(result.online()).isTrue();
    }

    @Test
    void 사용자_생성_이메일_또는_이름_중복_실패() {
        // given
        UserCreateRequest request = new UserCreateRequest("username", "password", "email@test.com");
        MultipartFile profile = null;
        given(userRepository.findByUsername("username")).willReturn(Optional.of(new User()));
        given(userRepository.findByEmail("email@test.com")).willReturn(Optional.of(new User()));

        // when & then
        assertThatThrownBy(() -> userService.save(request, profile))
            .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void 사용자_업데이트_성공() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID binaryContentId = UUID.randomUUID();

        UserUpdateRequest request = new UserUpdateRequest("username", "password", "email@test.com");
        MockMultipartFile profile = new MockMultipartFile("profile", "profile.png", "image/png",
            "fake-content".getBytes());

        User existingUser = new User();
        Field userIdField = BaseEntity.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(existingUser, userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(userRepository.findByEmail("email@test.com")).willReturn(Optional.empty());
        given(userRepository.findByUsername("username")).willReturn(Optional.empty());

        BinaryContent profileContent = new BinaryContent();
        Field profileIdField = BaseEntity.class.getDeclaredField("id");
        profileIdField.setAccessible(true);
        profileIdField.set(profileContent, binaryContentId);

        User updatedUser = new User("username", "password", "email@test.com", profileContent);
        userIdField.set(updatedUser, userId);

        given(userRepository.saveAndFlush(any(User.class))).willReturn(updatedUser);

        given(userMapper.toDto(any(User.class))).willReturn(
            new UserDto(
                userId,
                "username",
                "email@test.com",
                new BinaryContentDto(binaryContentId, profile.getOriginalFilename(),
                    (long) "fake-content".getBytes().length, profile.getContentType(),
                    profile.getBytes()),
                null
            )
        );

        // when
        UserDto result = userService.update(userId, request, profile);
        log.info("업데이트 반환 값: {}", result);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.username()).isEqualTo("username");
        assertThat(result.email()).isEqualTo("email@test.com");

        assertThat(result.profile().id()).isEqualTo(binaryContentId);
        assertThat(result.profile().fileName()).isEqualTo("profile.png");
        assertThat(result.profile().contentType()).isEqualTo("image/png");
        assertThat(result.profile().size()).isEqualTo("fake-content".getBytes().length);
        assertThat(result.profile().bytes()).isEqualTo("fake-content".getBytes());
    }

    @Test
    void 사용자_업데이트_아이디_찾기_실패() {
        // given
        UUID userId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> userService.update(userId, null, null))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 사용자_삭제_성공() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User("username", "password", "email@test.com", null);
        Field userIdField = BaseEntity.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(user, userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        userService.delete(user.getId());

        // then
        then(userRepository).should().delete(user);
    }

    @Test
    void 사용자_삭제_아이디_찾기_실패() {
        // given
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.delete(userId))
            .isInstanceOf(UserNotFoundException.class);
    }
}
