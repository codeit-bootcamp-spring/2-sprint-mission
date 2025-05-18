package com.sprint.mission.discodeit.service.test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/*
UserService : create, update, delete 의 성공, 실패의 testCase
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    // Mock 어노테이션을 사용하지 않을 경우엔 아래와 같이 구현한다.
    // UserRepository userRepository = mock(UserRepository.class);

    @Mock
    // private는 생략해도 상관없다. 의미적으로 표현하는 의미에서 추가해도 됨.
    private UserMapper userMapper;

    // test 대상 + Mock 주입
    // Mockito가 BasicUserService 실제 인스턴스를 만들고 Mock필드로 선언한 Mock을 주입한다.
    // 나머지 로직은 진짜 코드 실행
    @InjectMocks
    // private UserService userService = new BasicUserService();
    private BasicUserService userService;

    // Create 성공하는 테스트
    @Test
    @DisplayName("UserCreateTest_success")
    void createUser_success() {
        // given
        UserCreateRequest userCreateRequest = new UserCreateRequest(
            "testName", "wow@naver.com", "password");

        // mapper가 만들어 줄 실제 엔티티
        User user = new User(
            "testName", "wow@naver.com", "password", null);
        // save 호출 시 주입할 UUID
        UUID generatedId = UUID.randomUUID();
        // 기대(예상)하는 값
        UserDto userDto = new UserDto(
            generatedId, "testName", "wow@naver.com", null, false);

        // stub
        // 중복 이메일 없음
        given(userRepository.existsByEmail(userCreateRequest.email())).willReturn(false);
        // 중복 이름 없음
        given(userRepository.existsByUsername(userCreateRequest.username())).willReturn(false);

        // save가 호출되면 ID를 세팅해 반환 -> 저장되는 척(?)
        // 실제 JPA라면 save 뒤에 PK가 채워진다. 흉내낸 것
        given(userRepository.save(any(User.class))).willAnswer(invocationOnMock -> {
            User user1 = invocationOnMock.getArgument(0);
            ReflectionTestUtils.setField(user1, "id", generatedId);
            return user1;
        });

        // DTO로 변환결과를 준비해 놓는다.
        given(userMapper.toDto(user)).willReturn(userDto);

        // when
        // user 엔티티 생성 -> save 호출 -> userMapper.toDto()호출 -> stub이 excepted(DTO) 반환
        UserDto result = userService.create(
            userCreateRequest, Optional.empty());

        // then
        // 반환된 DTO가 예상했던 결과(expected)와 동일한지 검증
        assertThat(result).isEqualTo(userDto);
        // 상호작용 검증 (save가 정확히 1회 호출되었느지)
        then(userRepository).should().save(user);
    }

    @Test
    @DisplayName("UserCreateTest_fail_duplicateEmail")
    void createUser_fail_duplicateEmail() {
        // given
        UserCreateRequest request = new UserCreateRequest(
            "testName", "wow@naver.com", "password");
        // 이메일만 중복될 경우를 가정
        given(userRepository.existsByUsername(request.username())).willReturn(false);
        given(userRepository.existsByEmail(request.email())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> userService
            .create(request, Optional.empty()))
            .isInstanceOf(UserAlreadyExistsException.class);

        // save()가 호출되지 않았는지 never()로 확인.
        then(userRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("UserUpdateTest_success")
    void updateUser_success() {
        // given
        UUID uuid = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest(
            "kim", "newWow@naver.com", "newPassword");
        User user = new User(
            "kim", "newWow@naver.com", "newPassword", null);
        ReflectionTestUtils.setField(user, "id", uuid);

        given(userRepository.findById(uuid)).willReturn(Optional.of(user));
        given(userRepository.existsByUsername(request.newUsername())).willReturn(false);
        given(userRepository.existsByEmail(request.newEmail())).willReturn(false);

        // 예상(기대)하는 응답
        UserDto expected = new UserDto(
            uuid, "kim", "newWow@naver.com", null, false);
        given(userMapper.toDto(user)).willReturn(expected);

        // when
        UserDto result = userService.update(
            uuid, request, Optional.empty());

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("UserUpdateTest_fail_duplicateName")
    void updateUser_fail_duplicateUsername() {
        //given
        UUID id = UUID.randomUUID();
        UserUpdateRequest req = new UserUpdateRequest("dupName", "hong@test.com", "password");
        User entity = new User("hong", "hong@test.com", "password", null);

        given(userRepository.findById(id)).willReturn(Optional.of(entity));
        given(userRepository.existsByUsername(req.newUsername())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> userService.update(id, req, Optional.empty()))
            .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    @DisplayName("UserDeleteTest_success")
    void deleteUser_success() {
        // id가 존재하면 삭제
        UUID uuid = UUID.randomUUID();
        given(userRepository.existsById(uuid)).willReturn(true);

        // when
        userService.delete(uuid);

        // then
        then(userRepository).should().deleteById(uuid);
    }

    @Test
    @DisplayName("UserDeleteTest_fail_notFound")
    void deleteUser_fail_notFound() {
        UUID id = UUID.randomUUID();
        given(userRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> userService.delete(id))
            .isInstanceOf(UserNotFoundException.class);

        then(userRepository).should(never()).deleteById(any());
    }
}
