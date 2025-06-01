package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.IntegrationTestSupport;
import com.sprint.mission.discodeit.domain.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.domain.auth.exception.AuthPasswordNotMatchException;
import com.sprint.mission.discodeit.domain.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;


class BasicAuthServiceTest extends IntegrationTestSupport {

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
    @Value("${spring.datasource.url}")
    String url;


    @Test
    void printDatasourceUrl() {
        System.out.println("🔍 실제 연결된 DB URL: " + url);
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