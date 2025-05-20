package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;
    
    @Mock
    private UserStatusRepository userStatusRepository;
    
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private BinaryContentService binaryContentService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_EMAIL = "test@naver.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String UPDATED_USERNAME = "updatedUser";
    private static final String UPDATED_EMAIL = "updated@naver.com";
    private static final String UPDATED_PASSWORD = "newPassword123";
    private static final String PROFILE_FILENAME = "profile.jpg";
    private static final String UPDATED_PROFILE_FILENAME = "updated-profile.jpg";
    private static final String IMAGE_MIME_TYPE = "image/jpeg";
    private static final long FILE_SIZE = 1024L;

    private User user;
    private UserCreateRequest userCreateRequest;
    private UserUpdateRequest userUpdateRequest;
    private UserDto userDto;
    private UUID userId;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mockFile = mock(MultipartFile.class);

        user = new User(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD, null);

        userCreateRequest = new UserCreateRequest(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        userUpdateRequest = new UserUpdateRequest(UPDATED_USERNAME, UPDATED_EMAIL, UPDATED_PASSWORD);
        userDto = new UserDto(userId, TEST_USERNAME, TEST_EMAIL, null, true);
    }

    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_Success() {
        given(userRepository.existsByEmail(userCreateRequest.email())).willReturn(false);
        given(userRepository.existsByUsername(userCreateRequest.username())).willReturn(false);

        UUID binaryContentId = UUID.randomUUID();
        BinaryContentDto mockBinaryContentDto = new BinaryContentDto(binaryContentId, PROFILE_FILENAME, FILE_SIZE, IMAGE_MIME_TYPE);
        
        when(mockFile.isEmpty()).thenReturn(false);
        given(binaryContentService.create(mockFile)).willReturn(mockBinaryContentDto);
        
        BinaryContent mockBinaryContent = mock(BinaryContent.class);
        given(mockBinaryContent.getId()).willReturn(binaryContentId);
        given(binaryContentRepository.findById(binaryContentId)).willReturn(Optional.of(mockBinaryContent));

        User savedUser = new User(userCreateRequest.username(), userCreateRequest.email(), userCreateRequest.password(), mockBinaryContent);
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        given(userMapper.toDto(any(User.class))).willReturn(userDto);

        UserDto createdUserDto = userService.create(userCreateRequest, mockFile);

        assertThat(createdUserDto).isNotNull();
        assertThat(createdUserDto.username()).isEqualTo(userCreateRequest.username());
        assertThat(createdUserDto.email()).isEqualTo(userCreateRequest.email());
        verify(userRepository).existsByEmail(userCreateRequest.email());
        verify(userRepository).existsByUsername(userCreateRequest.username());
        verify(userRepository).save(any(User.class));
        verify(userStatusRepository).save(any());
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 실패 - 이미 존재하는 이메일")
    void createUser_Failure_DuplicateEmail() {
        given(userRepository.existsByEmail(userCreateRequest.email())).willReturn(true);

        assertThatThrownBy(() -> userService.create(userCreateRequest, mockFile))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    @DisplayName("사용자 생성 실패 - 이미 존재하는 사용자명")
    void createUser_Failure_DuplicateUsername() {
        given(userRepository.existsByEmail(userCreateRequest.email())).willReturn(false);
        given(userRepository.existsByUsername(userCreateRequest.username())).willReturn(true);

        assertThatThrownBy(() -> userService.create(userCreateRequest, mockFile))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    @DisplayName("사용자 정보 수정 성공")
    void updateUser_Success() {
        User existingUser = new User("oldUser", "old@naver.com", "oldPass", null);
        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(userRepository.existsByUsername(userUpdateRequest.newUsername())).willReturn(false);
        given(userRepository.existsByEmail(userUpdateRequest.newEmail())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(existingUser);
        
        UUID binaryContentId = UUID.randomUUID();
        BinaryContentDto mockBinaryContentDto = new BinaryContentDto(binaryContentId, UPDATED_PROFILE_FILENAME, FILE_SIZE, IMAGE_MIME_TYPE);
        given(binaryContentService.create(mockFile)).willReturn(mockBinaryContentDto);
        
        BinaryContent mockBinaryContent = mock(BinaryContent.class);
        given(mockBinaryContent.getId()).willReturn(binaryContentId);
        given(binaryContentRepository.findById(binaryContentId)).willReturn(Optional.of(mockBinaryContent));
        
        UserDto updatedDto = new UserDto(userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), null, true);
        given(userMapper.toDto(any(User.class))).willReturn(updatedDto);

        UserDto resultDto = userService.update(userId, userUpdateRequest, mockFile);

        assertThat(resultDto).isNotNull();
        assertThat(resultDto.username()).isEqualTo(userUpdateRequest.newUsername());
        assertThat(resultDto.email()).isEqualTo(userUpdateRequest.newEmail());
        verify(userRepository).findById(userId);
        verify(userRepository).existsByUsername(userUpdateRequest.newUsername());
        verify(userRepository).existsByEmail(userUpdateRequest.newEmail());
        verify(userRepository).save(existingUser);
        verify(userMapper).toDto(existingUser);
    }

    @Test
    @DisplayName("사용자 정보 수정 실패 - 존재하지 않는 사용자")
    void updateUser_Failure_UserNotFound() {
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(userId, userUpdateRequest, mockFile))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 정보 수정 실패 - 사용자명 중복")
    void updateUser_Failure_DuplicateUsername() {
        User existingUser = new User("oldUser", "old@naver.com", "oldPass", null);
        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(userRepository.existsByUsername(userUpdateRequest.newUsername())).willReturn(true);

        assertThatThrownBy(() -> userService.update(userId, userUpdateRequest, mockFile))
            .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    @DisplayName("사용자 정보 수정 실패 - 이메일 중복")
    void updateUser_Failure_DuplicateEmail() {
        User existingUser = new User("oldUser", "old@naver.com", "oldPass", null);
        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(userRepository.existsByUsername(userUpdateRequest.newUsername())).willReturn(false);
        given(userRepository.existsByEmail(userUpdateRequest.newEmail())).willReturn(true);

        assertThatThrownBy(() -> userService.update(userId, userUpdateRequest, mockFile))
            .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUser_Success() {
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("사용자 삭제 실패 - 존재하지 않는 사용자")
    void deleteUser_Failure_UserNotFound() {
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(userId))
            .isInstanceOf(UserNotFoundException.class);
    }
} 