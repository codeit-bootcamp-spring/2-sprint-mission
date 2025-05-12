package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.EmailAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
import org.springframework.test.util.ReflectionTestUtils;

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

    @Test
    @DisplayName("정상적인 User update 테스트")
    void updateUser_WithValidInput_Success() {
        User user = new User("user1", "user1@gmail.com", "1234", null);
        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
        UserUpdateRequest request = new UserUpdateRequest("user00","user00@gmail.com","4321");

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.existsByUsername(user.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(user.getEmail())).willReturn(false);

        userService.update(user.getId(),request,Optional.empty());

        assertEquals("user00", user.getUsername());
        assertEquals("user00@gmail.com", user.getEmail());
        assertEquals("4321", user.getPassword());
    }
    @Test
    @DisplayName("정상적인 User 프로필 update 테스트")
    void updateUser_Profile_WithValidInput_Success(){
        User user = new User("user1","user1@gmail.com","1234", null);
        ReflectionTestUtils.setField(user,"id",UUID.randomUUID());
        UserUpdateRequest request = new UserUpdateRequest("","","");

        Optional<BinaryContentCreateRequest> profile = Optional.of(new BinaryContentCreateRequest("profile.png", "image/png", "ImageData".getBytes()));

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.existsByUsername(user.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(user.getEmail())).willReturn(false);

        userService.update(user.getId(),request,profile);

        assertEquals("profile.png", user.getProfile().getFileName());
        assertEquals("image/png", user.getProfile().getContentType());
        assertEquals("ImageData".getBytes().length, user.getProfile().getSize());

        verify(binaryContentRepository).save(any());
        verify(binaryContentStorage).put(any(), any());
    }
    @Test
    @DisplayName("존재하지 않는 userId로 업데이트 시 예외 발생 테스트")
    void updateUser_WithInvalidUserId_ThrowException(){
        UUID noExistId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("user1","user1@gmail.com","0000");

        given(userRepository.findById(noExistId)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.update(noExistId,request,Optional.empty()));
    }
    @Test
    @DisplayName("이미 존재하는 Username로 User update 시 예외 발생 테스트")
    void updateUser_WithDuplicateUsername_ThrowException() {
        User user = new User("user1","user1@gmail.com","1234", null);
        ReflectionTestUtils.setField(user,"id",UUID.randomUUID());
        UserUpdateRequest request = new UserUpdateRequest("user10", "", "");
        Optional<BinaryContentCreateRequest> profile = Optional.empty();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.existsByUsername(request.newUsername())).willReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.update(user.getId(),request, profile));
    }
    @Test
    @DisplayName("이미 존재하는 email로 User update 시 예외 발생 테스트")
    void updateUser_WithDuplicateEmail_ThrowException(){
        User user = new User("user1","user1@gmail.com","1234", null);
        ReflectionTestUtils.setField(user,"id",UUID.randomUUID());
        UserUpdateRequest request = new UserUpdateRequest("", "user10@gmail.com", "");
        Optional<BinaryContentCreateRequest> profile = Optional.empty();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(request.newEmail())).willReturn(true);

        assertThrows(EmailAlreadyExistException.class, () -> userService.update(user.getId(),request, profile));
    }

}