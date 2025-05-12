package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.user.EmailAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BinaryContentMapper binaryContentMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("정상적인 User 생성 테스트")
    void createUser_WithValidInput_Success() {
        UserCreateRequest request = new UserCreateRequest("user1", "user1@gmail.com", "1234");
        Optional<BinaryContentCreateRequest> profile = Optional.empty();

        given(userRepository.existsByUsername(request.username())).willReturn(false);
        given(userRepository.existsByEmail(request.email())).willReturn(false);

        UserDto expectedDto = new UserDto(
                UUID.randomUUID(),
                "user1",
                "user1@gmail.com",
                null,
                true
        );
        given(userMapper.toDto(any())).willReturn(expectedDto);

        UserDto userDto = userService.create(request, profile);

        assertEquals("user1", userDto.username());
        assertEquals("user1@gmail.com", userDto.email());

        verify(userRepository).save(any());
        verify(userStatusRepository).save(any());

    }

    @Test
    @DisplayName("프로필이 있을 때 정상적인 User 생성 테스트")
    void createUser_IncludeProfile_WithValidInput_Success() {
        UserCreateRequest request = new UserCreateRequest("user2", "user2@gmail.com", "12345");
        Optional<BinaryContentCreateRequest> binaryContentCreateRequest = Optional.of(new BinaryContentCreateRequest("profile.png", "image/png", "ImageData".getBytes()));

        given(userRepository.existsByUsername(request.username())).willReturn(false);
        given(userRepository.existsByEmail(request.email())).willReturn(false);

        BinaryContentDto profile = new BinaryContentDto(
                UUID.randomUUID(),
                binaryContentCreateRequest.get().fileName(),
                (long) binaryContentCreateRequest.get().bytes().length,
                binaryContentCreateRequest.get().contentType()
        );

        given(binaryContentMapper.toDto(any())).willReturn(profile);

        UserDto expectedDto = new UserDto(
                UUID.randomUUID(),
                "user2",
                "user2@gmail.com",
                profile,
                true
        );

        given(userMapper.toDto(any())).willReturn(expectedDto);

        UserDto userDto = userService.create(request, binaryContentCreateRequest);

        assertEquals("user2", userDto.username());
        assertEquals("user2@gmail.com", userDto.email());
        assertEquals(profile, userDto.profile());

        verify(userRepository).save(any());
        verify(binaryContentRepository).save(any());
        verify(binaryContentStorage).put(any(), any());
        verify(userStatusRepository).save(any());

    }

    @Test
    @DisplayName("이미 존재하는 Username로 User Create시 예외 발생 테스트")
    void createUser_WithDuplicateUsername_ThrowException() {
        UserCreateRequest request = new UserCreateRequest("user1", "user1@gmail.com", "1234");
        Optional<BinaryContentCreateRequest> profile = Optional.empty();

        given(userRepository.existsByUsername(request.username())).willReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.create(request, profile));
    }

    @Test
    @DisplayName("이미 존재하는 email로 User Create시 예외 발생 테스트")
    void createUser_WithDuplicateEmail_ThrowException() {
        UserCreateRequest request = new UserCreateRequest("user1", "user1@gmail.com", "1234");
        Optional<BinaryContentCreateRequest> profile = Optional.empty();

        given(userRepository.existsByEmail(request.email())).willReturn(true);

        assertThrows(EmailAlreadyExistException.class, () -> userService.create(request, profile));
    }
}