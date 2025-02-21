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
    private static final String PASSWORD = "password";
    private static final String NAME = "황지환";
    private UserService userService;
    private UserDto setUpUser;

    @BeforeEach
    void init() {
        userService = new JCFUserService();

        this.setUpUser = userService.register(new UserRegisterDto(NAME, PASSWORD));
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