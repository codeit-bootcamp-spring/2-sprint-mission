package com.sprint.mission.discodeit.unit;

import com.sprint.mission.discodeit.Mapper.UserMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateUserEmail;
import com.sprint.mission.discodeit.exception.user.DuplicateUserUserName;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BinaryContentRepository binaryContentRepository;
    @Mock
    UserStatusRepository userStatusRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    BinaryContentStorage binaryContentStorage;

    @InjectMocks
    BasicUserService userService;

    @Test
    @DisplayName("유저 생성 성공")
    void createSuccess() {
        // 1) given: 테스트를 위해 목(Mock) 객체들의 동작을 정의
        UserCreateRequest req = new UserCreateRequest("user123", "user123example.com", "userPassword");

        // save() 호출 시 반환할 가짜 User 엔티티
        User saved = new User("user123", "user123@example.com", "userPassword", null);

        // 이메일·사용자명 중복 체크가 모두 false(중복 없음)라고 설정
        given(userRepository.existsByEmail(req.email())).willReturn(false);
        given(userRepository.existsByUsername(req.username())).willReturn(false);
        // save(any(User.class)) 호출되면 saved 객체를 반환하게 설정
        given(userRepository.save(any(User.class))).willReturn(saved);

        // 엔티티 → DTO 매핑(mock) 결과도 미리 정의w
        UserDto dto = new UserDto(UUID.randomUUID(), "user123", "userPassword", null, false);
        given(userMapper.toDto(saved)).willReturn(dto);

        // 2) when: 실제 서비스 메서드를 호출
        UserDto result = userService.create(req, Optional.empty());

        // 3) then: 기대하는 상호작용과 결과를 검증
        // → userRepository.save()가 정확히 한 번 호출됐는지 확인
        then(userRepository).should().save(any(User.class));
        // → userMapper.toDto()도 saved 객체로 한 번 호출됐는지 확인
        then(userMapper).should().toDto(saved);
        // → 최종 반환된 result가 우리가 미리 설정한 dto와 같은지 검증
        assertThat(result).isEqualTo(dto);
    }

    @Test
    @DisplayName("유저 이메일 중복 체크")
    void createFailDuplicateEmail() {
        // given
        UserCreateRequest req = new UserCreateRequest("user123", "wlsghdid2@", "user123@example.com");
        given(userRepository.existsByEmail(req.email())).willReturn(true);

        // when
        Throwable thrown = catchThrowable(() -> userService.create(req, Optional.empty()));

        // then
        assertThat(thrown).isInstanceOf(DuplicateUserEmail.class);
    }

    @Test
    @DisplayName("유저 업데이트 성공")
    void updateSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest req = new UserUpdateRequest("user123", "user123@example.com", "userPassword");
        User saved = new User("user123", "user123@example.com", "userPassword", null);

        given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(new User()));
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(saved);

        UserDto dto = new UserDto(UUID.randomUUID(), "user123", "userPassword", null, false);
        given(userMapper.toDto(saved)).willReturn(dto);

        // when
        UserDto result = userService.update(userId, req, Optional.empty());

        // then
        // 메서드 호출 검증
        then(userRepository).should().save(any(User.class));
        then(userMapper).should().toDto(any(User.class));

        // 반환값 검증
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("유저 이름 중복체크")
    void updateFailDuplicateUsername() {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest req = new UserUpdateRequest("user123", "user123@example.com", "userPassword");
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(new User()));
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.existsByUsername(anyString())).willReturn(true);

        // when
        Throwable thrown = catchThrowable( () -> userService.update(userId, req, Optional.empty()));

        // then
        assertThat(thrown).isInstanceOf(DuplicateUserUserName.class);
    }

    @Test
    @DisplayName("삭제 성공")
    void deleteSuccess() {
        // given
        UUID id = UUID.randomUUID();
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(new User("user123", "user123@example.com", "userPassword", null)));

        // when
        // 예외가 없음을 검증
        assertDoesNotThrow(() -> userService.delete(id));

        // then
        then(userRepository).should().deleteById(id);
    }

    @Test
    @DisplayName("삭제 실패")
    void deleteFailNotFound() {
        // given
        UUID id = UUID.randomUUID();
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> userService.delete(id));

        // then
        assertThat(thrown).isInstanceOf(UserNotFoundException.class);


    }
}
