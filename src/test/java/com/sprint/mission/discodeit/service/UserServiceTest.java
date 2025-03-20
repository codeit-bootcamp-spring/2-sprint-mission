package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {
    private UserService userService;
    private UserDto setUpUser;

    @BeforeEach
    void setUp() {
        userService = new BasicUserService(new JCFUserRepository(), new JCFUserStatusRepository());
        setUpUser = userService.register(
                new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword()), null);
    }

    @AfterEach
    void tearDown() {
        userService.delete(setUpUser.id());
    }

    @DisplayName("유저 등록시 중복된 이메일이 있을 경우 예외 처리합니다")
    @Test
    void registerDuplicateEmail() {
        UserRegisterDto sameEmailUser = new UserRegisterDto(OTHER_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword());
        assertThatThrownBy(() -> {
            userService.register(sameEmailUser, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유저 등록시 중복된 유저 이름이 있을 경우 예외 처리합니다")
    @Test
    void registerDuplicateUserName() {
        UserRegisterDto sameNameUser = new UserRegisterDto(LONGIN_USER.getName(), OTHER_USER.getEmail(), LONGIN_USER.getPassword());
        assertThatThrownBy(() -> {
            userService.register(sameNameUser, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 유저_UUID_단건_조회_예외테스트() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 유저_UUID_단건_조회() {
        UUID id = setUpUser.id();
        UserDto userDto = userService.findById(id);

        assertThat(userDto.id()).isEqualTo(id);
    }

    @Test
    void 유저_이름_단건_조회() {
        UserDto user = userService.findByName(LONGIN_USER.getName());

        assertThat(user.name()).isEqualTo(LONGIN_USER.getName());
    }

    @Test
    void 유저_이름_수정() {
        String userName = "김철수";
        userService.updateName(setUpUser.id(), userName);

        UserDto updatedUserInfo = userService.findById(setUpUser.id());

        assertThat(setUpUser.name()).isNotEqualTo(updatedUserInfo.name());
    }

    @Test
    void 유저_삭제() {
        UUID id = setUpUser.id();
        userService.delete(id);

        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}