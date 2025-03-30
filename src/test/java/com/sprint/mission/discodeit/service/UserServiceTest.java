package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private UserResult setUpUser;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        userService = new BasicUserService(userRepository, userStatusRepository);
        setUpUser = userService.register(new UserRequest(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null);
    }

    @DisplayName("유저 등록시 중복된 이메일이 있을 경우 예외 처리합니다")
    @Test
    void registerDuplicateEmail() {
        assertThatThrownBy(() -> userService.register(new UserRequest(OTHER_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유저 등록시 중복된 유저 이름이 있을 경우 예외 처리합니다")
    @Test
    void registerDuplicateUserName() {
        assertThatThrownBy(() -> userService.register(new UserRequest(LOGIN_USER.getName(), OTHER_USER.getEmail(), LOGIN_USER.getPassword()), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 UUID로 유저 조회 시 예외 발생")
    @Test
    void findByIdThrowsExceptionWhenUserNotFound() {
        assertThatThrownBy(() -> userService.findById(UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("UUID로 유저 단건 조회")
    @Test
    void findByIdReturnsUser() {
        UserResult userResult = userService.findById(setUpUser.id());
        assertThat(userResult.id()).isEqualTo(setUpUser.id());
    }

    @DisplayName("이름으로 유저 단건 조회")
    @Test
    void findByNameReturnsUser() {
        UserResult user = userService.findByName(LOGIN_USER.getName());
        assertThat(user.name()).isEqualTo(LOGIN_USER.getName());
    }

    @DisplayName("유저 이름 수정")
    @Test
    void updateUserName() {
        String newUserName = "김철수";
        userService.updateName(setUpUser.id(), newUserName);
        UserResult updatedUserInfo = userService.findById(setUpUser.id());
        assertThat(updatedUserInfo.name()).isEqualTo(newUserName);
    }

    @DisplayName("유저 삭제 후 조회 시 예외 발생")
    @Test
    void deleteUserAndVerifyNotFound() {
        userService.delete(setUpUser.id());
        assertThatThrownBy(() -> userService.findById(setUpUser.id()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}