package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.application.UserDto;
import com.sprint.mission.application.UserRegisterDto;
import com.sprint.mission.discodeit.jcf.JCFUserService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {
    private static UserService userService;
    private static final String NAME = "황지환";
    private static final String PASSWORD = "password";
    private UserDto registeredUserInfo;

    @BeforeEach
    void init() {
        userService = new JCFUserService();

        UserRegisterDto userRegisterDto = new UserRegisterDto(NAME, PASSWORD);
        registeredUserInfo = userService.register(userRegisterDto);
    }

    @Test
    void 유저_UUID_단건_조회_예외테스트() {
        assertThatThrownBy(() -> userService.findById(UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 유저_UUID_단건_조회() {
        UUID id = registeredUserInfo.id();
        UserDto userDto = userService.findById(id);

        assertThat(userDto.id()).isEqualTo(id);
    }

    @Test
    void 유저_이름_단건_조회() {
        List<UserDto> users = userService.findByName(NAME);
        UserDto userDto = users.get(0);

        assertThat(userDto.name()).isEqualTo(NAME);
    }

    @Test
    void 유저_이름_다수_조회() {
        UserRegisterDto userOtherHwang = new UserRegisterDto(NAME, PASSWORD + "123");
        UserRegisterDto userKim = new UserRegisterDto("KIM", PASSWORD);

        userService.register(userOtherHwang);
        userService.register(userKim);

        assertThat(userService.findByName(NAME)).hasSize(2);
    }
}