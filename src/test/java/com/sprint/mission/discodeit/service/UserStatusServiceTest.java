package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.mock.user.UserInfo.LOGIN_USER;
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
        user = userRepository.save(
                new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
    }

    @DisplayName("처음 생성시 유저의 로그인 상태는 true 반환합니다.")
    @Test
    void create() {
        UserStatusResult userStatusDto = userStatusService.create(new UserStatusCreateRequest(user.getId(), Instant.now()));
        assertThat(userStatusDto.online()).isTrue();
    }

    @DisplayName("존재하지 않는 유저 ID로 생성 시 예외를 발생시킨다.")
    @Test
    void create_NotUser() {
        UUID random = UUID.randomUUID();

        assertThatThrownBy(() -> userStatusService.create(new UserStatusCreateRequest(random, Instant.now())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 존재하는 유저 상태로 다시 생성 시 예외를 발생시킨다.")
    @Test
    void create_AlreadyUserStatusExist() {
        userStatusService.create(new UserStatusCreateRequest(user.getId(), Instant.now()));

        assertThatThrownBy(() -> userStatusService.create(new UserStatusCreateRequest(user.getId(), Instant.now())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유저 ID로 상태를 조회하면 올바른 상태를 반환한다.")
    @Test
    void findByUserId() {
        UserStatusResult userStatusDto = userStatusService.create(new UserStatusCreateRequest(user.getId(), Instant.now()));
        UserStatusResult userStatusDto1 = userStatusService.getByUserId(user.getId());

        assertThat(userStatusDto.id()).isEqualTo(userStatusDto1.id());
    }

    @DisplayName("유저 ID로 상태를 업데이트하면 로그인 시간이 갱신된다.")
    @Test
    void updateByUserId() {
        userStatusService.create(new UserStatusCreateRequest(user.getId(), Instant.now()));
        Instant now = Instant.now();
        UserStatusResult updatedUserStatusDto = userStatusService.updateByUserId(user.getId(), new UserStatusUpdateRequest(now));

        assertThat(updatedUserStatusDto.lastLoginAt()).isEqualTo(now);
    }

    @DisplayName("현재 시간으로 유저 ID로 상태를 업데이트할 경우, online은 true를 반환한다")
    @Test
    void updateByUserId_isOnline() {
        userStatusService.create(new UserStatusCreateRequest(user.getId(), Instant.now()));
        Instant now = Instant.now();
        UserStatusResult updatedUserStatusDto = userStatusService.updateByUserId(user.getId(), new UserStatusUpdateRequest(now));

        assertThat(updatedUserStatusDto.online()).isTrue();
    }
}