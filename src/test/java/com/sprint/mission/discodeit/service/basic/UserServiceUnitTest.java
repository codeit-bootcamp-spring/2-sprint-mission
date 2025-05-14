package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.ErrorCode;
import com.sprint.mission.discodeit.exceptions.user.DuplicateUserOrEmailException;
import com.sprint.mission.discodeit.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ResponseMapStruct;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserJPARepository userJpaRepository;

    @Mock
    private BinaryContentJPARepository binaryContentJpaRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private ResponseMapStruct responseMapStruct;

    @InjectMocks
    private BasicUserService basicUserService;


    // 유저 생성 테스트
    @Test
    @DisplayName("[User][create] user 생성 테스트")
    public void testCreateUser() {
        UserCreateDto user = new UserCreateDto("Test", "test@test.com", "12345");
        given(userJpaRepository.existsByUsernameOrEmail("Test", "test@test.com")).willReturn(false);

        User saveUser = new User("Test", "Test@test.com", "12345", null);
        given(userJpaRepository.save(any(User.class))).willReturn(saveUser);

        UserResponseDto responseUser = new UserResponseDto(
                UUID.randomUUID(),
                saveUser.getUsername(),
                saveUser.getEmail(),
                null,
                true
        );
        given(responseMapStruct.toUserDto(any(User.class))).willReturn(responseUser);

        UserResponseDto result = basicUserService.create(user, Optional.empty());

        then(userJpaRepository).should().existsByUsernameOrEmail("Test", "test@test.com");
        then(userJpaRepository).should().save(any(User.class));
        then(responseMapStruct).should().toUserDto(any(User.class));

        assertEquals(responseUser, result);
        assertEquals("Test", result.username());
        assertEquals("Test@test.com", result.email());
    }

    @Test
    @DisplayName("[User][create] 이름 및 이메일 중복 생성 시 예외 테스트")
    public void testUpdateUserWithDuplicateUserOrEmailException() {
        UserCreateDto user = new UserCreateDto("Test", "test@test.com", "12345");
        given(userJpaRepository.existsByUsernameOrEmail("Test", "test@test.com")).willReturn(true);

        DuplicateUserOrEmailException exception = assertThrows(DuplicateUserOrEmailException.class,
                ()-> basicUserService.create(user, Optional.empty()));

        then(userJpaRepository).should().existsByUsernameOrEmail("Test", "test@test.com");

        assertEquals(ErrorCode.DUPLICATE_USER_OR_EMAIL, exception.getErrorCode());

        then(userJpaRepository).should(never()).save(any(User.class));
    }

    // 유저 수정 테스트
    @Test
    @DisplayName("[User][update] user 수정 테스트")
    public void testUpdateUser() {
        UserUpdateDto updateUser = new UserUpdateDto("kkk", "kkk@kkk.com", "kkk1234");

        UUID userId = UUID.randomUUID();
        User savedUser = new User("Test", "Test@test.com", "12345", null);
        ReflectionTestUtils.setField(savedUser, "id", userId);
        given(userJpaRepository.findById(userId)).willReturn(Optional.of(savedUser));
        given(userJpaRepository.save(any(User.class))).willReturn(savedUser);

        UserResponseDto responseUser = new UserResponseDto(
                userId,
                updateUser.newUsername(),
                updateUser.newEmail(),
                null,
                true
        );
        given(responseMapStruct.toUserDto(any(User.class))).willReturn(responseUser);

        UserResponseDto result = basicUserService.update(userId, updateUser, Optional.empty());

        then(userJpaRepository).should().findById(userId);
        then(userJpaRepository).should().save(any(User.class));
        then(responseMapStruct).should().toUserDto(any(User.class));

        assertEquals(responseUser, result);
        assertEquals("kkk", result.username());
        assertEquals("kkk@kkk.com", result.email());
    }

    @Test
    @DisplayName("[User][update] 존재하지 않는 사용자로 수정 예외 테스트")
    public void testUpdateUserWithUserNotFoundException() {
        UserUpdateDto updateUser = new UserUpdateDto("kkk", "kkk@kkk.com", "kkk1234");
        UUID userId = UUID.randomUUID();
        User savedUser = new User("Test", "Test@test.com", "12345", null);

        given(userJpaRepository.findById(userId)).willReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                ()-> basicUserService.update(userId, updateUser, Optional.empty()));

        then(userJpaRepository).should().findById(userId);

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        then(userJpaRepository).should(never()).save(any(User.class));
    }


    // 유제 삭제 테스트
    @Test
    @DisplayName("[User][delete] user 삭제 테스트")
    public void testDeleteUser() {
        UUID userId = UUID.randomUUID();
        User savedUser = new User("Test", "Test@test.com", "12345", null);
        ReflectionTestUtils.setField(savedUser, "id", userId);
        given(userJpaRepository.findById(userId)).willReturn(Optional.of(savedUser));

        basicUserService.delete(userId);

        then(userJpaRepository).should().delete(any(User.class));
    }

    @Test
    @DisplayName("[User][delete] 존재하지 않는 사용자 삭제 예외 테스트")
    public void testDeleteUserWithUserNotFoundException() {
        UUID userId = UUID.randomUUID();
        User savedUser = new User("Test", "Test@test.com", "12345", null);

        given(userJpaRepository.findById(userId)).willReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                ()-> basicUserService.delete(userId));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        then(userJpaRepository).should(never()).delete(any(User.class));



    }
}