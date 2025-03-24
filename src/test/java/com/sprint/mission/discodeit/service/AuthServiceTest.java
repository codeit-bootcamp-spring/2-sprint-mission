package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserLoginDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {
    private AuthService authService;
    private UserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        UserService userService = new BasicUserService(userRepository, userStatusRepository);
        userService.register(new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null);
        authService = new BasicAuthService(userRepository, userStatusRepository);
    }

    @DisplayName("로그인을 하면 유저정보를 반환하고 UserStatus를 변경합니다.")
    @Test
    void login() {
        UserLoginDto loginRequestUser = new UserLoginDto(LOGIN_USER.getName(), LOGIN_USER.getPassword());
        UserDto user = authService.login(loginRequestUser);
        UserStatus userStatus = userStatusRepository.findByUserId(user.id()).get();

        assertThat(userStatus.isLogin(ZonedDateTime.now().toInstant())).isTrue();
    }

    @DisplayName("로그인시 등록된 유저가 없으면 예외를 반환합니다.")
    @Test
    void loginException() {
        UserLoginDto loginRequestUser = new UserLoginDto(OTHER_USER.getName(), OTHER_USER.getPassword());
        assertThatThrownBy(() -> authService.login(loginRequestUser))
                .isInstanceOf(IllegalArgumentException.class);
    }
}