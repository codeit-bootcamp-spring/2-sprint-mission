package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserLoginDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;

class AuthControllerTest {
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private AuthService authService;
    private UserDto setUpUser;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        UserService userService = new BasicUserService(userRepository, userStatusRepository);

        UserRegisterDto userRegisterDto = new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.name(), LOGIN_USER.getPassword());
        setUpUser = userService.register(userRegisterDto, null);

        authService = new BasicAuthService(userRepository, userStatusRepository);
        authService.login(new UserLoginDto(LOGIN_USER.getName(), LOGIN_USER.getPassword()));
    }

    @DisplayName("로그인 시 유저 상태 저장 체크")
    @Test
    void loginTest() {
        User user = userRepository.findById(setUpUser.id()).orElseThrow();
        UserStatus userStatus = userStatusRepository.findByUserId(setUpUser.id()).orElseThrow();

        assertThat(user.getUserStatus().isLogin() && userStatus.isLogin()).isTrue();
    }
}