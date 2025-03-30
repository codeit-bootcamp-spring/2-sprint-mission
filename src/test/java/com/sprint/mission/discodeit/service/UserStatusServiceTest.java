package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserStatusServiceTest {

    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private UserStatusService userStatusService;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        userStatusService = new BasicUserStatusService(userStatusRepository, userRepository);
        user = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
    }

    @DisplayName("처음 생성시 유저의 로그인 상태는 false를 반환합니다.")
    @Test
    void create() {
        UserStatusResult userStatusDto = userStatusService.create(user.getId());
        assertThat(userStatusDto.isLogin()).isFalse();
    }

    @DisplayName("존재하지 않는 유저 ID로 생성 시 예외를 발생시킨다.")
    @Test
    void create_NotUser() {
        UUID random = UUID.randomUUID();
        userStatusRepository.save(new UserStatus(random));

        assertThatThrownBy(() -> userStatusService.create(random)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 존재하는 유저 상태로 다시 생성 시 예외를 발생시킨다.")
    @Test
    void create_AlreadyUserStatusExist() {
        userStatusService.create(user.getId());

        assertThatThrownBy(() -> userStatusService.create(user.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유저 ID로 상태를 조회하면 올바른 상태를 반환한다.")
    @Test
    void findByUserId() {
        UserStatusResult userStatusDto = userStatusService.create(user.getId());
        UserStatusResult userStatusDto1 = userStatusService.getByUserId(user.getId());

        assertThat(userStatusDto.id()).isEqualTo(userStatusDto1.id());
    }

    @DisplayName("유저 ID로 상태를 업데이트하면 로그인 시간이 갱신된다.")
    @Test
    void updateByUserId() {
        UserStatusResult userStatusDto = userStatusService.create(user.getId());
        UserStatusResult updatedUserStatusDto = userStatusService.updateByUserId(user.getId());

        assertThat(updatedUserStatusDto.lastLoginAt()).isAfter(userStatusDto.lastLoginAt());
    }
}