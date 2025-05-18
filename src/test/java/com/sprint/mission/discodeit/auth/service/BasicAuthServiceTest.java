package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.auth.exception.AuthPasswordNotMatchException;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
class BasicAuthServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private AuthService authService;

    @AfterEach
    void tearDown() {
        userStatusRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("이름과 비밀번호로 로그인 합니다.")
    @Test
    void login() {
        // given
        String name = UUID.randomUUID().toString();
        String email = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        User savedUser = userRepository.save(new User(name, email, password, null));
        LoginRequest loginRequest = new LoginRequest(name, password);

        // when
        UserResult loginUser = authService.login(loginRequest);

        // then
        Assertions.assertThat(loginUser)
                .extracting(UserResult::id, UserResult::username)
                .containsExactlyInAnyOrder(savedUser.getId(), savedUser.getName());
    }

    @DisplayName("로그인시 등록된 이름이 없다면, 예외를 반환합니다.")
    @Test
    void login_NotRegisteredUser() {
        // given
        User savedUser = userRepository.save(new User(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "", null));
        LoginRequest loginRequest = new LoginRequest(UUID.randomUUID().toString(), "");

        // when & then
        Assertions.assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("로그인시 비밀번호가 일치하지 않으면, 예외를 반환합니다.")
    @Test
    void login_NotValidatePassword() {
        // given
        String name = UUID.randomUUID().toString();
        String email = UUID.randomUUID().toString();
        User savedUser = userRepository.save(new User(name, email, "", null));
        LoginRequest loginRequest = new LoginRequest(name, UUID.randomUUID().toString());

        // when & then
        Assertions.assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(AuthPasswordNotMatchException.class);
    }

    @DisplayName("로그인시 유저 상태가 없을 경우, 예외를 반환합니다.")
    @Test
    void login_NotUserStatus() {
        // given
        String name = UUID.randomUUID().toString();
        String email = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        User savedUser = userRepository.save(new User(name, email, password, null));
        LoginRequest loginRequest = new LoginRequest(name, password);
        userStatusRepository.deleteById(savedUser.getUserStatus().getId());

        // when & then
        Assertions.assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(UserStatusNotFoundException.class);
    }

}